package com.github.shenzhang.ejdbc.builder;

import com.google.common.base.Strings;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * User: shenzhang
 * Date: 8/23/14
 * Time: 7:59 PM
 */
public class UpdateSqlBuilder implements SqlBuilder {
    private StringBuilder sb = new StringBuilder();
    private List<String> columns = newArrayList();
    private String where;

    public UpdateSqlBuilder(String table) {
        sb.append("UPDATE ").append(table).append(" SET ");
    }

    public void appendColumn(String column) {
        columns.add(column);
    }

    public void setWhere(String where) {
        this.where = where;
    }

    @Override
    public String create() {
        if (columns.isEmpty()) {
            throw new RuntimeException("Should has one column at least");
        }

        String sep = "";
        for (String column : columns) {
            sb.append(sep).append(column).append("=?");
            sep = ",";
        }

        if (!Strings.isNullOrEmpty(where)) {
            sb.append(" WHERE ").append(where);
        }

        return sb.toString();
    }
}
