package com.github.shenzhang.ejdbc.rowMapper.primitive;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 9:25 PM
 */
public class LongRowMapper implements RowMapper<Long> {
    @Override
    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong(1);
    }
}
