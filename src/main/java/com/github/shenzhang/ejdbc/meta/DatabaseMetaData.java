package com.github.shenzhang.ejdbc.meta;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import temple.sql.config.feature.PageCreator;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import static com.google.common.collect.Maps.newConcurrentMap;
import static com.google.common.collect.Maps.newHashMap;
import static temple.sql.config.GlobalConfiguration.getGlobalConfiguration;

/**
 * User: shenzhang
 * Date: 8/23/14
 * Time: 10:56 PM
 */
public class DatabaseMetaData {
    private static Map<DataSource, Map<String, TableMetaData>> metaDataCache = newConcurrentMap();
    private MetaDataExtractor extractor = new MetaDataExtractor();
    private JdbcTemplate jdbcTemplate;
    private PageCreator pageCreator;

    public DatabaseMetaData(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.pageCreator = getGlobalConfiguration().getConfiguration(jdbcTemplate.getDataSource()).getPageCreator();
    }

    public TableMetaData getTableColumns(String table) {
        table = table.toUpperCase().trim();
        return getSqlColumns("SELECT * FROM " + table);
    }

    public TableMetaData getSqlColumns(String sql, Object... parameters) {
        TableMetaData cachedTableMeta = getCachedTableMeta(sql);
        if (cachedTableMeta != null) {
            return cachedTableMeta;
        }

        String selectSql = pageCreator != null ? pageCreator.createPage(sql, 0, 1) : sql;
        TableMetaData values = jdbcTemplate.query(selectSql, extractor, parameters);
        cacheTableMeta(sql, values);

        return values;
    }

    private TableMetaData getCachedTableMeta(String sql) {
        Map<String, TableMetaData> map = metaDataCache.get(jdbcTemplate.getDataSource());
        if (map == null) {
            return null;
        }

        return map.get(sql);
    }

    private void cacheTableMeta(String sql, TableMetaData meta) {
        Map<String, TableMetaData> map = metaDataCache.get(jdbcTemplate.getDataSource());
        if (map == null) {
            map = newConcurrentMap();
            metaDataCache.put(jdbcTemplate.getDataSource(), map);
        }

        map.put(sql, meta);
    }

    private static class MetaDataExtractor implements ResultSetExtractor<TableMetaData> {
        @Override
        public TableMetaData extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, ColumnMetaData> value = newHashMap();

            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                String name = metaData.getColumnName(i);
//                int type = metaData.getColumnType(i);
                value.put(name, new ColumnMetaData(name, null));
            }

            return new TableMetaData(value);
        }
    }
}