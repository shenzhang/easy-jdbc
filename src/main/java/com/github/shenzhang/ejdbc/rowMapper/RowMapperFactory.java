package com.github.shenzhang.ejdbc.rowMapper;

import com.github.shenzhang.ejdbc.config.feature.NameConvertor;
import com.github.shenzhang.ejdbc.meta.DatabaseMetaData;
import com.github.shenzhang.ejdbc.meta.TableMetaData;
import com.github.shenzhang.ejdbc.rowMapper.complex.ReflectRowMapper;
import com.github.shenzhang.ejdbc.rowMapper.extractor.ResultSetValueExtractor;
import com.github.shenzhang.ejdbc.rowMapper.simple.SimpleTypeRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static com.github.shenzhang.ejdbc.config.GlobalConfiguration.get;
import static com.google.common.collect.Maps.newHashMap;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 9:10 PM
 */
public class RowMapperFactory {
    private DatabaseMetaData metaData;
    private RowMapperCache rowMapperCache = new RowMapperCache();

    public RowMapperFactory(JdbcTemplate jdbcTemplate) {
        this.metaData = new DatabaseMetaData(jdbcTemplate);
    }

    public <T> RowMapper<T> createRowMapper(QueryInformation<T> queryInformation) {
        Class<T> clazz = queryInformation.getClazz();

        RowMapper<T> rowMapper = createSimpleRowMapper(clazz);
        if (rowMapper != null) {
            return rowMapper;
        }

        return createComplexRowMapper(queryInformation);
    }

    private <T> RowMapper<T> createSimpleRowMapper(Class<T> clazz) {
        ResultSetValueExtractor resultSetValueExtractor = ReflectRowMapper.map.get(clazz);
        if (resultSetValueExtractor == null) {
            return null;
        }

        return new SimpleTypeRowMapper<T>(resultSetValueExtractor);
    }

    private <T> RowMapper<T> createComplexRowMapper(QueryInformation<T> queryInformation) {
        RowMapper<T> rowMapper = rowMapperCache.get(queryInformation);
        if (rowMapper == null) {
            TableMetaData sqlColumns = metaData.getSqlColumns(queryInformation.getSql(), queryInformation.getParameters());
            List<String> columns = sqlColumns.getColumns();
            Map<String, Field> map = newHashMap();
            NameConvertor nameConvertor = get().getConfiguration(queryInformation.getDataSource()).getNameConvertor();

            Class<T> clazz = queryInformation.getClazz();
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
