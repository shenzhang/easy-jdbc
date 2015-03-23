package com.github.shenzhang.ejdbc.config;

import org.springframework.beans.factory.InitializingBean;
import temple.sql.config.feature.GeneratedKeyFetcher;
import temple.sql.config.feature.NameConvertor;
import temple.sql.config.feature.PageCreator;

import javax.sql.DataSource;

import static temple.sql.config.GlobalConfiguration.getGlobalConfiguration;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 5:53 PM
 */
public class SpringConfigBean implements InitializingBean {
    private DataSource dataSource;
    private PageCreator pageCreator;
    private NameConvertor nameConvertor;
    private GeneratedKeyFetcher generatedKeyFetcher;

    @Override
    public void afterPropertiesSet() throws Exception {
        Configuration configuration = getGlobalConfiguration().getConfiguration(dataSource);
        if (pageCreator != null) {
            configuration.setPageCreator(pageCreator);
        }
        if (nameConvertor != null) {
            configuration.setNameConvertor(nameConvertor);
        }

        if (generatedKeyFetcher != null) {
            configuration.setKeyFetcher(generatedKeyFetcher);
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setPageCreator(PageCreator pageCreator) {
        this.pageCreator = pageCreator;
    }

    public void setNameConvertor(NameConvertor nameConvertor) {
        this.nameConvertor = nameConvertor;
    }

    public void setGeneratedKeyFetcher(GeneratedKeyFetcher generatedKeyFetcher) {
        this.generatedKeyFetcher = generatedKeyFetcher;
    }
}
