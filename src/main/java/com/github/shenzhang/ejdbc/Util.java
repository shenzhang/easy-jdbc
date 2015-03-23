package com.github.shenzhang.ejdbc;

import com.github.shenzhang.ejdbc.annotation.Table;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 9:14 PM
 */
public class Util {
    public static String getTableName(Class<?> clazz) {
        Table table = null;
        while (table == null && !clazz.equals(Object.class)) {
            table = clazz.getAnnotation(Table.class);
            clazz = clazz.getSuperclass();
        }


        if (table == null) {
            throw new RuntimeException("Can't retrieve table information");
        }

        return table.value();
    }
}
