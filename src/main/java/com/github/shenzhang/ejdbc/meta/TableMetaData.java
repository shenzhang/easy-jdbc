package com.github.shenzhang.ejdbc.meta;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

/**
 * User: shenzhang
 * Date: 8/23/14
 * Time: 11:39 PM
 */
public final class TableMetaData {
    private Map<String, ColumnMetaData> meta = newLinkedHashMap();
    public TableMetaData(Map<String, ColumnMetaData> meta) {
        for (Map.Entry<String, ColumnMetaData> entry : meta.entrySet()) {
            this.meta.put(entry.getKey().toUpperCase(), entry.getValue());
        }
    }

    public boolean hasColumn(String column) {
        return meta.containsKey(column.toUpperCase());
    }

    public List<String> getColumns() {
        return newArrayList(meta.keySet());
    }

    public ColumnMetaData getColumnMeta(String column) {
        return meta.get(column.toUpperCase());
    }
}
