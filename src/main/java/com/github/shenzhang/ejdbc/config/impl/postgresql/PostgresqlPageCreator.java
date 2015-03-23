package com.github.shenzhang.ejdbc.config.impl.postgresql;

import temple.sql.config.feature.PageCreator;

/**
 * User: shenzhang
 * Date: 8/30/14
 * Time: 8:44 PM
 */
public class PostgresqlPageCreator implements PageCreator {
    @Override
    public String createPage(String sql, int offset, int limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from (").append(sql).append(")");
        sb.append(" offset ").append(offset).append(" limit ").append(limit);
        return sb.toString();
    }
}
