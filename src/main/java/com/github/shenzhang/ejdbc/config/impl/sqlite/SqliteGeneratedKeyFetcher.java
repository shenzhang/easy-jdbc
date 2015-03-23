package com.github.shenzhang.ejdbc.config.impl.sqlite;

import com.github.shenzhang.ejdbc.JdbcTemplateEnhancement;
import com.github.shenzhang.ejdbc.config.feature.GeneratedKeyFetcher;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 8:51 PM
 */
public class SqliteGeneratedKeyFetcher implements GeneratedKeyFetcher {
    @Override
    public long getGeneratedKey(JdbcTemplateEnhancement jdbcTemplateEnhancement, String table, String column) {
        Integer seq = jdbcTemplateEnhancement.queryForObject(Integer.class, "select seq from sqlite_sequence where UPPER(name) = ?", table);
        return seq == null ? 1 : seq;
    }
}
