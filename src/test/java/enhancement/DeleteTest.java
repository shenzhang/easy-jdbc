package enhancement;

import base.AbstractIntegrationTest;
import model.User;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: Zhang Shen
 * Date: 3/25/15
 * Time: 2:37 PM
 */
public class DeleteTest extends AbstractIntegrationTest {
    @Test
    public void shouldExecuteDeleteStatementSuccess() throws Exception {
        User user = new User();
        user.setName("gege");
        user.setAge(30);
        jdbcTemplateEnhancement.insert("t_user", user);

        int count = jdbcTemplateEnhancement.queryForObject(Integer.class, "select count(*) from t_user");
        assertThat(count, is(1));

        jdbcTemplateEnhancement.execute("delete from t_user");
        count = jdbcTemplateEnhancement.queryForObject(Integer.class, "select count(*) from t_user");
        assertThat(count, is(0));
    }
}
