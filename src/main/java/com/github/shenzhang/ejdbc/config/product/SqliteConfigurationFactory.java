package com.github.shenzhang.ejdbc.config.product;

import temple.sql.config.Configuration;
import temple.sql.config.impl.sqlite.SqliteGeneratedKeyFetcher;
import temple.sql.config.impl.sqlite.SqlitePageCreator;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 8:30 PM
 */
public class SqliteConfigurationFactory implements ProductConfigurationFactory {
    @Override
    public boolean support(DatabaseMetaData database) {
        try {
            String name = database.getDatabaseProductName();
            if (name != null && name.toLowerCase().contains("sqlite")) {
                return true;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    @Override
    public Configuration createConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPageCreator(new SqlitePageCreator());
        configuration.setKeyFetcher(new SqliteGeneratedKeyFetcher());
        return configuration;
    }
}
