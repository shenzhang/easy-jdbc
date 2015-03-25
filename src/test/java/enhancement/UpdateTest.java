package enhancement;

import base.AbstractIntegrationTest;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * User: Zhang Shen
 * Date: 3/23/15
 * Time: 3:43 PM
 */
public class UpdateTest extends AbstractIntegrationTest {
    @Before
    public void setUp() throws Exception {
        executeNativeSql("insert into t_user(id, name, age) values(1, 'zhang shen', 30)");
    }

    @Test
    public void shouldUpdateEveryFields() throws Exception {
        User user = query();
        user.setName("zshen");
        user.setAge(28);
        Date now = new Date();
        user.setDob(now);
        user.setNullValue(7);

        jdbcTemplateEnhancement.update("t_user", user, "id = ?", user.getId());
        user = query();
        assertThat(user.getName(), is("zshen"));
        assertThat(user.getAge(), is(28));
        assertNotNull(user.getDob());
        assertThat(user.getNullValue(), is(7));
    }

    @Test
    public void shouldUpdateAllFieldsExceptExclutionFields() throws Exception {
        User user = query();
        user.setName("zshen");
        user.setAge(28);
        Date now = new Date();
        user.setDob(now);
        user.setNullValue(7);

        jdbcTemplateEnhancement.update("t_user", user, newArrayList("dob", "age"), "id = ?", user.getId());
        user = query();
        assertThat(user.getName(), is("zshen"));
        assertThat(user.getNullValue(), is(7));
        assertThat(user.getAge(), is(30));
        assertNull(user.getDob());
    }

    @Test
    public void shouldUpdateDateSuccessful() throws Exception {
        User user = query();
        Date now = new Date();
        user.setDob(now);
        jdbcTemplateEnhancement.update("t_user", user, "id = ?", user.getId());
        user = query();
        assertThat(user.getDob(), is(now));
    }

    private User query() {
        return jdbcTemplateEnhancement.queryForObject(User.class, "select * from t_user where id = 1");
    }
}
