package com.github.shenzhang.ejdbc.config.product;

import com.github.shenzhang.ejdbc.config.Configuration;

import java.sql.DatabaseMetaData;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 8:37 PM
 */
public interface ProductConfigurationFactory {
    boolean support(DatabaseMetaData database);

    Configuration createConfiguration();
}
