package com.github.shenzhang.ejdbc.config;

import temple.sql.config.feature.GeneratedKeyFetcher;
import temple.sql.config.feature.NameConvertor;
import temple.sql.config.feature.PageCreator;
import temple.sql.config.impl.DefaultNameConvertor;

/**
 * User: shenzhang
 * Date: 9/6/14
 * Time: 5:58 PM
 */
public class Configuration {
    private PageCreator pageCreator;
    private GeneratedKeyFetcher keyFetcher;
    private NameConvertor nameConvertor = new DefaultNameConvertor();

    public PageCreator getPageCreator() {
        return pageCreator;
    }

    public void setPageCreator(PageCreator pageCreator) {
        this.pageCreator = pageCreator;
    }

    public GeneratedKeyFetcher getKeyFetcher() {
        return keyFetcher;
    }

    public void setKeyFetcher(GeneratedKeyFetcher keyFetcher) {
        this.keyFetcher = keyFetcher;
    }

    public NameConvertor getNameConvertor() {
        return nameConvertor;
    }

    public void setNameConvertor(NameConvertor nameConvertor) {
        this.nameConvertor = nameConvertor;
    }
}
