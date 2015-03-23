package com.github.shenzhang.ejdbc.config;

import com.github.shenzhang.ejdbc.config.feature.GeneratedKeyFetcher;
import com.github.shenzhang.ejdbc.config.feature.NameConvertor;
import com.github.shenzhang.ejdbc.config.feature.PageCreator;
import com.github.shenzhang.ejdbc.config.impl.DefaultNameConvertor;

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
