package base;

import com.github.shenzhang.ejdbc.JdbcTemplateEnhancement;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Zhang Shen
 * Date: 3/23/15
 * Time: 11:24 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractIntegrationTest {
    @Autowired
    protected JdbcTemplateEnhancement jdbcTemplateEnhancement;

    @Autowired
    protected DataSource dataSource;

    public void executeNativeSql(String sql) {
        try {
            Connection connection = DataSourceUtils.getConnection(dataSource);
            connection.createStatement().execute(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
