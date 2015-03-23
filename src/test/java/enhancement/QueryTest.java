package enhancement;

import base.AbstractIntegrationTest;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: Zhang Shen
 * Date: 3/23/15
 * Time: 11:27 AM
 */
public class QueryTest extends AbstractIntegrationTest {
    @Test
    public void shouldReturnCount() throws Exception {
        int count = jdbcTemplateEnhancement.queryForObject(Integer.class, "select count(*) from t_user");
        assertThat(count, is(0));

        executeNativeSql("insert into t_user values(1, 'zhang shen', 30)");
        count = jdbcTemplateEnhancement.queryForObject(Integer.class, "select count(*) from t_user");
        assertThat(count, is(1));
    }
}
