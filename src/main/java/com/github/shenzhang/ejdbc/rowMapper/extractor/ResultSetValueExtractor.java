package com.github.shenzhang.ejdbc.rowMapper.extractor;

import java.sql.ResultSet;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 7:24 PM
 */
public interface ResultSetValueExtractor {
    Object extract(ResultSet rs, String column);
}
