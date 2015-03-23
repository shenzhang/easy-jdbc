package com.github.shenzhang.ejdbc.rowMapper;


import com.google.common.base.Objects;

import javax.sql.DataSource;

/**
 * User: shenzhang
 * Date: 8/31/14
 * Time: 10:04 AM
 */
public class QueryInformation<T> {
    private DataSource dataSource;
    private Class<T> clazz;
    private String sql;
    private Object[] parameters;

    public QueryInformation(DataSource dataSource, Class<T> clazz, String sql, Object... parameters) {
        this.dataSource = dataSource;
        this.clazz = clazz;
        this.sql = sql;
        this.parameters = parameters;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public String getSql() {
        return sql;
    }

    public Object[] getParameters() {
        return parameters;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dataSource, clazz, sql);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof QueryInformation)) {
            return false;
        }

        QueryInformation key = (QueryInformation)obj;
        return Objects.equal(key.dataSource, dataSource)
                && Objects.equal(key.clazz, clazz)
                && Objects.equal(key.sql, sql);
    }
}
