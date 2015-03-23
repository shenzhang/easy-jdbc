package com.github.shenzhang.ejdbc;

import com.github.shenzhang.ejdbc.builder.InsertSqlBuilder;
import com.github.shenzhang.ejdbc.builder.UpdateSqlBuilder;
import com.github.shenzhang.ejdbc.config.Configuration;
import com.github.shenzhang.ejdbc.config.GlobalConfiguration;
import com.github.shenzhang.ejdbc.config.feature.GeneratedKeyFetcher;
import com.github.shenzhang.ejdbc.config.feature.NameConvertor;
import com.github.shenzhang.ejdbc.meta.DatabaseMetaData;
import com.github.shenzhang.ejdbc.meta.TableMetaData;
import com.github.shenzhang.ejdbc.rowMapper.QueryInformation;
import com.github.shenzhang.ejdbc.rowMapper.RowMapperFactory;
import com.google.common.base.Strings;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.shenzhang.ejdbc.config.GlobalConfiguration.getGlobalConfiguration;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

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

    public void insert(String table, Object object, String... excludeColumns) {
        Map<String, Object> columnsAndValues = calculateFinalColumnsAndValues(table, object, excludeColumns);

        InsertSqlBuilder build = new InsertSqlBuilder(table);
        List<Object> parameters = newArrayList();
        for (Map.Entry<String, Object> column : columnsAndValues.entrySet()) {
            build.appendColumn(column.getKey());
            parameters.add(column.getValue());
        }

        jdbcTemplate.update(build.create(), parameters.toArray());
    }

    public long insertAndReturnGeneratedKey(String table, Object object, String keyColumn, String... excludeColumns) {
        Map<String, Object> columnsAndValues = calculateFinalColumnsAndValues(table, object, excludeColumns);

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.setTableName(table);
        insert.setGeneratedKeyName(keyColumn);
        insert.usingColumns(columnsAndValues.keySet().toArray(new String[columnsAndValues.size()]));

        try {
            Number number = insert.executeAndReturnKey(columnsAndValues);
            return number.longValue();
        } catch (Exception e) {
            Configuration configuration = getGlobalConfiguration().getConfiguration(jdbcTemplate.getDataSource());
            GeneratedKeyFetcher keyFetcher = configuration.getKeyFetcher();
            if (keyFetcher != null) {
                insert(table, object, excludeColumns);
                return keyFetcher.getGeneratedKey(this, table, keyColumn);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(String tableName, Object object, String where, Object... whereParameters) {
        this.update(tableName, object, null, where, whereParameters);
    }

    public void update(String table, Object object, Collection<String> excludeColumns, String where, Object... whereParameters) {
        Map<String, Object> columnsAndValues = calculateFinalColumnsAndValues(table, object, excludeColumns);
        UpdateSqlBuilder builder = new UpdateSqlBuilder(table);
        List<Object> parameters = newArrayList();
        for (Map.Entry<String, Object> entry : columnsAndValues.entrySet()) {
            builder.appendColumn(entry.getKey());
            parameters.add(entry.getValue());
        }

        if (!Strings.isNullOrEmpty(where)) {
            builder.setWhere(where);
            parameters.addAll(newArrayList(whereParameters));
        }

        jdbcTemplate.update(builder.create(), parameters.toArray());
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

    private Map<String, Object> calculateFinalColumnsAndValues(String table, Object object, Collection<String> excludeColumns) {
        Set<String> excludesSet = newHashSet();
        if (excludeColumns != null) {
            for (String column : excludeColumns) {
                excludesSet.add(column.toUpperCase());
            }
        }

        return calculateFinalColumnsAndValues(table, object, excludesSet.toArray(new String[excludesSet.size()]));
    }

    // return maps like: column -> field value
    private Map<String, Object> calculateFinalColumnsAndValues(String table, Object object, String... excludeColumns) {
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
