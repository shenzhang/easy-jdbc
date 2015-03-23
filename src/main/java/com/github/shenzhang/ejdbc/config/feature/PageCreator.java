package com.github.shenzhang.ejdbc.config.feature;

/**
 * User: shenzhang
 * Date: 8/30/14
 * Time: 8:43 PM
 */
public interface PageCreator {
    String createPage(String sql, int offset, int limit);
}
