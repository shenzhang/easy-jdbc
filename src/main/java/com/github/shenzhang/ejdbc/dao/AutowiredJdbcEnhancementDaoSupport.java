package com.github.shenzhang.ejdbc.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import temple.sql.JdbcTemplateEnhancement;

/**
 * User: shenzhang
 * Date: 8/31/14
 * Time: 9:35 PM
 */
public abstract class AutowiredJdbcEnhancementDaoSupport extends AutowiredJdbcDaoSupport {
    protected JdbcTemplateEnhancement jdbcEnhancement;
    protected JdbcTemplate jdbcTemplate;

    @Override
    protected void initTemplateConfig() {
        super.initTemplateConfig();
        this.jdbcTemplate = getJdbcTemplate();
        this.jdbcEnhancement = new JdbcTemplateEnhancement(this.jdbcTemplate);
    }
}
