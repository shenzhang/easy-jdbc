package com.github.shenzhang.ejdbc.config.feature;

import com.github.shenzhang.ejdbc.JdbcTemplateEnhancement;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 6:13 PM
 */
public interface GeneratedKeyFetcher {
    long getGeneratedKey(JdbcTemplateEnhancement jdbcTemplateEnhancement, String table, String column);
}
