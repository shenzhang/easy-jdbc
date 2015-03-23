package com.github.shenzhang.ejdbc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * User: shenzhang
 * Date: 8/20/14
 * Time: 8:11 PM
 */
public abstract class AutowiredJdbcDaoSupport extends JdbcDaoSupport {
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    void init() {
        setDataSource(dataSource);
    }
}
