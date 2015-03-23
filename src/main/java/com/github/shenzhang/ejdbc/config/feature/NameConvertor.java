package com.github.shenzhang.ejdbc.config.feature;

/**
 * User: shenzhang
 * Date: 9/7/14
 * Time: 10:41 PM
 */
public interface NameConvertor {
    String column2Field(String columnName);

    String field2Column(String filed);
}
