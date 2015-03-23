package com.github.shenzhang.ejdbc.rowMapper.simple;

import com.github.shenzhang.ejdbc.rowMapper.extractor.ResultSetValueExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Zhang Shen
 * Date: 3/23/15
 * Time: 3:26 PM
 */
public class SimpleTypeRowMapper<T> implements RowMapper<T> {
    private ResultSetValueExtractor extractor;

    public SimpleTypeRowMapper(ResultSetValueExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        String columnName = rs.getMetaData().getColumnName(1);
        return (T) extractor.extract(rs, columnName);
    }
}
