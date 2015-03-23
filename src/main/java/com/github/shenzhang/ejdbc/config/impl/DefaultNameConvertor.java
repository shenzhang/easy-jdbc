package com.github.shenzhang.ejdbc.config.impl;

import temple.sql.config.feature.NameConvertor;

/**
 * User: shenzhang
 * Date: 9/7/14
 * Time: 10:42 PM
 */
public class DefaultNameConvertor implements NameConvertor {
    @Override
    public String column2Field(String columnName) {
        StringBuilder sb = new StringBuilder();
        boolean underscore = false;
        for (char c : columnName.toCharArray()) {
            if (c == '_') {
                underscore = true;
                continue;
            }

            sb.append(underscore ? Character.toUpperCase(c) : Character.toLowerCase(c));
            underscore = false;
        }

        return sb.toString();
    }

    @Override
    public String field2Column(String filed) {
        StringBuilder sb = new StringBuilder(filed.length());
        for (char c : filed.toCharArray()) {
            if (c >= 'A' && c <= 'Z' && sb.length() > 0) {
                sb.append('_');
            }
            sb.append(c);
        }
        return sb.toString().toUpperCase();
    }
}
