package com.github.shenzhang.ejdbc.rowMapper.complex;

import com.github.shenzhang.ejdbc.rowMapper.extractor.CalendarValueExtractor;
import org.springframework.jdbc.core.RowMapper;
import com.github.shenzhang.ejdbc.rowMapper.extractor.DateValueExtractor;
import com.github.shenzhang.ejdbc.rowMapper.extractor.DefaultResultSetValueExtractor;
import com.github.shenzhang.ejdbc.rowMapper.extractor.ResultSetValueExtractor;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * User: shenzhang
 * Date: 8/31/14
 * Time: 10:26 AM
 */
public class ReflectRowMapper<T> implements RowMapper<T> {
    public static final Map<Class<?>, ResultSetValueExtractor> map = newHashMap();

    static {
        map.put(String.class, new DefaultResultSetValueExtractor("getString"));
        map.put(int.class, new DefaultResultSetValueExtractor("getInt"));
        map.put(Integer.class, new DefaultResultSetValueExtractor("getInt"));
        map.put(long.class, new DefaultResultSetValueExtractor("getLong"));
        map.put(Long.class, new DefaultResultSetValueExtractor("getLong"));
        map.put(Boolean.class, new DefaultResultSetValueExtractor("getBoolean"));
        map.put(boolean.class, new DefaultResultSetValueExtractor("getBoolean"));
        map.put(Date.class, new DateValueExtractor());
        map.put(Calendar.class, new CalendarValueExtractor());
    }

    private Class<T> clazz;
    private Map<String, Field> column2FieldMapper;

    public ReflectRowMapper(Class<T> clazz, Map<String, Field> column2FieldMapper) {
        this.clazz = clazz;
        this.column2FieldMapper = column2FieldMapper;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T object;
        try {
            object = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Can not instance query object: " + clazz.toString(), e);
        }

        for (String column : column2FieldMapper.keySet()) {
            Field field = column2FieldMapper.get(column);
            Object value = getTargetValue(rs, column, field.getType());
            try {
                field.set(object, value);
            } catch (Exception e) {
                throw new RuntimeException("Set value error for column: " + column, e);
            }
        }

        return object;
    }

    private Object getTargetValue(ResultSet rs, String columnName, Class<?> fieldClass) throws SQLException {
        ResultSetValueExtractor extractor = map.get(fieldClass);
        if (extractor == null) {
            throw new RuntimeException("Can not find ResultSet value extractor for class: " + fieldClass.getName());
        }

        Object value;
        try {
            value = extractor.extract(rs, columnName);
        } catch (Exception e) {
            throw new RuntimeException("Extract value from ResultSet failed for class: " + fieldClass.getName(), e);
        }
        return value;
    }
}
