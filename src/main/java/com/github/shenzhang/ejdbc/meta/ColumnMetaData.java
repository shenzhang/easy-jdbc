package com.github.shenzhang.ejdbc.meta;

/**
 * User: shenzhang
 * Date: 8/30/14
 * Time: 8:31 PM
 */

class ColumnMetaData {
    private String columnName;
    private Class<?> type;

    public ColumnMetaData(String name, Class<?> type) {
        this.columnName = name;
        this.type = type;
    }

    public String getColumnName() {
        return columnName;
    }

    public Class<?> getType() {
        return type;
    }
}
