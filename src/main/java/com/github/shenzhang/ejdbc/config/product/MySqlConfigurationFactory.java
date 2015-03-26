package com.github.shenzhang.ejdbc.config.product;

import com.github.shenzhang.ejdbc.config.Configuration;
import com.github.shenzhang.ejdbc.config.impl.mysql.MySqlPageCreator;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 8:30 PM
 */
public class MySqlConfigurationFactory implements ProductConfigurationFactory {
    @Override
    public boolean support(DatabaseMetaData database) {
        try {
            String name = database.getDatabaseProductName();
            if (name != null && name.toLowerCase().contains("mysql")) {
                return true;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    @Override
    public Configuration createConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPageCreator(new MySqlPageCreator());
        return configuration;
    }
}
