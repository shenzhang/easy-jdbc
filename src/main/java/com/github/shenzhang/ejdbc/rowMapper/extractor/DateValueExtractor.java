package com.github.shenzhang.ejdbc.rowMapper.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 7:28 PM
 */
public class DateValueExtractor implements ResultSetValueExtractor {
    @Override
    public Object extract(ResultSet rs, String column) {
        try {
            Timestamp date = rs.getTimestamp(column);
            return date == null ? null : new java.util.Date(date.getTime());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
