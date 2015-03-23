package com.github.shenzhang.ejdbc;

import com.google.common.base.Strings;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import temple.sql.builder.InsertSqlBuilder;
import temple.sql.builder.UpdateSqlBuilder;
import temple.sql.config.Configuration;
import temple.sql.config.GlobalConfiguration;
import temple.sql.config.feature.GeneratedKeyFetcher;
import temple.sql.config.feature.NameConvertor;
import temple.sql.meta.DatabaseMetaData;
import temple.sql.meta.TableMetaData;
import temple.sql.rowMapper.QueryInformation;
import temple.sql.rowMapper.RowMapperFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static temple.sql.Util.getTableName;
import static temple.sql.config.GlobalConfiguration.getGlobalConfiguration;

/**
 * User: shenzhang
 * Date: 8/23/14
 * Time: 7:48 PM
 */
public class JdbcTemplateEnhancement {
    private DatabaseMetaData metaData;
    private RowMapperFactory rowMapperFactory;
    private JdbcTemplate jdbcTemplate;
    private NameConvertor nameConvertor;

    public JdbcTemplateEnhancement(JdbcTemplate template) {
        this.jdbcTemplate = template;
        this.metaData = new DatabaseMetaData(jdbcTemplate);
        this.rowMapperFactory = new RowMapperFactory(jdbcTemplate);
        this.nameConvertor = GlobalConfiguration.getGlobalConfiguration().getConfiguration(jdbcTemplate.getDataSource()).getNameConvertor();
    }

    JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void insert(Object object, String... excludeColumns) {
        Map<String, Object> columnsAndValues = calculateFinalColumnsAndValues(object, excludeColumns);

        InsertSqlBuilder build = new InsertSqlBuilder(Util.getTableName(object.getClass()));
        List<Object> parameters = newArrayList();
        for (Map.Entry<String, Object> column : columnsAndValues.entrySet()) {
            build.appendColumn(column.getKey());
            parameters.add(column.getValue());
        }

        jdbcTemplate.update(build.create(), parameters.toArray());
    }

    public long insertAndReturnGeneratedKey(String keyColumn, Object object, String... excludeColumns) {
        Map<String, Object> columnsAndValues = calculateFinalColumnsAndValues(object, excludeColumns);

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        String tableName = Util.getTableName(object.getClass());
        insert.setTableName(tableName);
        insert.setGeneratedKeyName(keyColumn);
        insert.usingColumns(columnsAndValues.keySet().toArray(new String[columnsAndValues.size()]));

        try {
            Number number = insert.executeAndReturnKey(columnsAndValues);
            return number.longValue();
        } catch (Exception e) {
            Configuration configuration = getGlobalConfiguration().getConfiguration(jdbcTemplate.getDataSource());
            GeneratedKeyFetcher keyFetcher = configuration.getKeyFetcher();
            if (keyFetcher != null) {
                insert(object, excludeColumns);
                return keyFetcher.getGeneratedKey(this, tableName, keyColumn);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(Object object, String where, Object... whereParameters) {
        this.update(object, null, where, whereParameters);
    }

    public void update(Object object, Collection<String> excludeColumns, String where, Object... whereParameters) {
        Map<String, Object> columnsAndValues = calculateFinalColumnsAndValues(object, excludeColumns);

        String table = getTableName(object.getClass());
        UpdateSqlBuilder build = new UpdateSqlBuilder(table);
        List<Object> parameters = newArrayList();
        for (Map.Entry<String, Object> entry : columnsAndValues.entrySet()) {
            build.appendColumn(entry.getKey());
            parameters.add(entry.getValue());
        }

        if (!Strings.isNullOrEmpty(where)) {
            build.setWhere(where);
            parameters.addAll(newArrayList(whereParameters));
        }

        jdbcTemplate.update(build.create(), parameters.toArray());
    }

    public <T> List<T> queryForList(Class<T> clazz, String sql, Object... parameters) {
        QueryInformation<T> queryInformation = new QueryInformation<T>(jdbcTemplate.getDataSource(), clazz, sql, parameters);
        RowMapper<T> rowMapper = rowMapperFactory.createRowMapper(queryInformation);
        return jdbcTemplate.query(sql, parameters, rowMapper);
    }

    public <T> T queryForObject(Class<T> clazz, String sql, Object... parameters) {
        List<T> list = queryForList(clazz, sql, parameters);
        return list.isEmpty() ? null : list.get(0);
    }

    public JdbcTempalteAppender createAppender() {
        return new JdbcTempalteAppender(this);
    }

    private Map<String, Object> calculateFinalColumnsAndValues(Object object, Collection<String> excludeColumns) {
        Set<String> excludesSet = newHashSet();
        if (excludeColumns != null) {
            for (String column : excludeColumns) {
                excludesSet.add(column.toUpperCase());
            }
        }

        return calculateFinalColumnsAndValues(object, excludesSet.toArray(new String[excludesSet.size()]));
    }

    // return maps like: column -> field value
    private Map<String, Object> calculateFinalColumnsAndValues(Object object, String... excludeColumns) {
        Set<String> excludesSet = newHashSet();
        if (excludeColumns != null) {
            for (String column : excludeColumns) {
                excludesSet.add(column.toUpperCase());
            }
        }

        Map<String, String> properties;
        try {
            properties = BeanUtils.describe(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String table = getTableName(object.getClass());
        TableMetaData tableColumns = metaData.getTableColumns(table);
        Set<String> exitingColumns = newHashSet(tableColumns.getColumns());

        Map<String, Object> result = newHashMap();
        try {
            for (String property : properties.keySet()) {
                String column = nameConvertor.field2Column(property);
                if (!excludesSet.contains(column) && exitingColumns.contains(column)) {
                    result.put(column, BeanUtilsBean.getInstance().getPropertyUtils().getProperty(object, property));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
