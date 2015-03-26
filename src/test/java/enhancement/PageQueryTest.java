package enhancement;

import base.AbstractIntegrationTest;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: Zhang Shen
 * Date: 3/25/15
 * Time: 2:43 PM
 */
public class PageQueryTest extends AbstractIntegrationTest {
    @Before
    public void setUp() throws Exception {
        String insert = "insert into t_user(id, name, age) values(%d, '%s', %d)";
        for (int i = 1; i <= 30; i++) {
            executeNativeSql(String.format(insert, i, "name" + i, i));
        }
    }

    @Test
    public void shouldReturnPagedResultsFrom11To20() throws Exception {
        List<User> users = jdbcTemplateEnhancement.pageForList(User.class, 10, 10, "select * from t_user order by id");
        assertThat(users.size(), is(10));
        for (int i = 0; i < 10; i++) {
            assertThat(users.get(i).getId(), is(i + 11L));
        }
    }

    @Test
    public void shouldReturnFirst5Records() throws Exception {
        List<User> users = jdbcTemplateEnhancement.pageForList(User.class, 0, 5, "select * from t_user order by id");
        assertThat(users.size(), is(5));
        for (int i = 0; i < 5; i++) {
            assertThat(users.get(i).getId(), is(i + 1L));
        }
    }
}
