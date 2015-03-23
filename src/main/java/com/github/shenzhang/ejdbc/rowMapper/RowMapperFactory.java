package com.github.shenzhang.ejdbc.rowMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import temple.sql.config.feature.NameConvertor;
import temple.sql.meta.DatabaseMetaData;
import temple.sql.meta.TableMetaData;
import temple.sql.rowMapper.primitive.IntegerRowMapper;
import temple.sql.rowMapper.primitive.LongRowMapper;
import temple.sql.rowMapper.primitive.StringRowMapper;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static temple.sql.config.GlobalConfiguration.getGlobalConfiguration;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 9:10 PM
 */
public class RowMapperFactory {
    private DatabaseMetaData metaData;
    private RowMapperCache rowMapperCache = new RowMapperCache();
    private static Map<Class<?>, RowMapper> simpleTypes = newHashMap();

    static {
        simpleTypes.put(Integer.class, new IntegerRowMapper());
        simpleTypes.put(int.class, new IntegerRowMapper());
        simpleTypes.put(Long.class, new LongRowMapper());
        simpleTypes.put(long.class, new LongRowMapper());
        simpleTypes.put(String.class, new StringRowMapper());
    }

    public RowMapperFactory(JdbcTemplate jdbcTemplate) {
        this.metaData = new DatabaseMetaData(jdbcTemplate);
    }

    public <T> RowMapper<T> createRowMapper(QueryInformation<T> queryInformation) {
        DataSource dataSource = queryInformation.getDataSource();
        Class<T> clazz = queryInformation.getClazz();
        String sql = queryInformation.getSql();

        RowMapper<T> rowMapper = simpleTypes.get(clazz);
        if (rowMapper != null) {
            return rowMapper;
        }

        rowMapper = rowMapperCache.get(queryInformation);
        if (rowMapper == null) {
            TableMetaData sqlColumns = metaData.getSqlColumns(sql, queryInformation.getParameters());
            List<String> columns = sqlColumns.getColumns();
            Map<String, Field> map = newHashMap();
            NameConvertor nameConvertor = getGlobalConfiguration().getConfiguration(dataSource).getNameConvertor();
            for (String column : columns) {
                String property = nameConvertor.column2Field(column);
                try {
                    Field field = clazz.getDeclaredField(property);
                    field.setAccessible(true);
                    map.put(column, field);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException("Can not find correct field from class: " + clazz.getName() + " according to column: " + column);
                }
            }

            rowMapper = new ReflectRowMapper<T>(clazz, map);
            rowMapperCache.add(queryInformation, rowMapper);
        }

        return rowMapper;
    }
}
