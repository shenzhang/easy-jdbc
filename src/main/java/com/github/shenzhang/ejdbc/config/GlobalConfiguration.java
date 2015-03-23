package com.github.shenzhang.ejdbc.config;

import temple.sql.config.product.ProductConfigurationFactory;
import temple.sql.config.product.SqliteConfigurationFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 5:54 PM
 */
public class GlobalConfiguration {
    private static GlobalConfiguration globalConfiguration = new GlobalConfiguration();
    private static Object addConfigurationLocker = new Object();
    private Map<DataSource, Configuration> configurations = new HashMap<DataSource, Configuration>();
    private List<ProductConfigurationFactory> productFactoryList = newArrayList();

    private GlobalConfiguration() {
        productFactoryList.add(new SqliteConfigurationFactory());
    }

    public static GlobalConfiguration getGlobalConfiguration() {
        return globalConfiguration;
    }

    public Configuration getConfiguration(DataSource dataSource) {
        Configuration configuration = configurations.get(dataSource);
        if (configuration == null) {
            synchronized (addConfigurationLocker) {
                if (configuration == null) {
                    configuration = createConfigurationBasedOnProduct(dataSource);
                    if (configuration == null) {
                        configuration = new Configuration();
                    }
                    configurations.put(dataSource, configuration);
                }
            }
        }

        return configuration;
    }

    public void setConfiguration(DataSource dataSource, Configuration configuration) {
        synchronized (addConfigurationLocker) {
            configurations.put(dataSource, configuration);
        }
    }

    /**
     * For testing
     */
    void reset() {
        globalConfiguration.configurations.clear();
    }

    private Configuration createConfigurationBasedOnProduct(DataSource dataSource) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            for (ProductConfigurationFactory factory : productFactoryList) {
                if (factory.support(metaData)) {
                    return factory.createConfiguration();
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}