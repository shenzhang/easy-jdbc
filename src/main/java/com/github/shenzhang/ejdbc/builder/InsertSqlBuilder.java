package com.github.shenzhang.ejdbc.builder;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * User: shenzhang
 * Date: 8/23/14
 * Time: 7:59 PM
 */
public class InsertSqlBuilder implements SqlBuilder {
    private StringBuilder sb = new StringBuilder();
    private List<String> columns = newArrayList();

    public InsertSqlBuilder(String table) {
        sb.append("INSERT INTO ").append(table);
    }

    public void appendColumn(String column) {
        columns.add(column);
    }

    @Override
    public String create() {
        if (columns.isEmpty()) {
            throw new RuntimeException("Should has one column at least");
        }

        sb.append("(");
        Joiner.on(",").appendTo(sb, columns);
        sb.append(") ");

        sb.append("VALUES(");
        String values = Strings.repeat("?,", columns.size());
        sb.append(values.substring(0, values.length() - 1));
        sb.append(");");

        return sb.toString();
    }
}
