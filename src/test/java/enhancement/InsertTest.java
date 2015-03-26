package enhancement;

import base.AbstractIntegrationTest;
import model.User;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: Zhang Shen
 * Date: 3/23/15
 * Time: 3:43 PM
 */
public class InsertTest extends AbstractIntegrationTest {
    @Test
    public void shouldInsertAllMatchingFields() throws Exception {
        User user = new User();
//        user.setId(100);
        user.setName("gege");
        user.setAge(20);
        jdbcTemplateEnhancement.insert("t_user", user);
        User actual = jdbcTemplateEnhancement.queryForObject(User.class, "select * from t_user");
        int count = jdbcTemplateEnhancement.queryForObject(Integer.class, "select count(*) from t_user");
        assertThat(count, is(1));
        assertThat(actual, is(user));
    }

    @Test
    public void shouldInsertWithoutExcludedFields() throws Exception {
        User user = new User();
//        user.setId(100);
        user.setName("gege");
        user.setAge(20);
        user.setNullValue(7);
        jdbcTemplateEnhancement.insert("t_user", user, "null_value");
        User actual = jdbcTemplateEnhancement.queryForObject(User.class, "select * from t_user");
        int count = jdbcTemplateEnhancement.queryForObject(Integer.class, "select count(*) from t_user");
        assertThat(count, is(1));
        user.setNullValue(null);
        assertThat(actual, is(user));
    }

    @Test
    public void shouldReturnGeneratedIdAfterInsert() throws Exception {
        User user = new User();
        user.setName("gege");
        user.setAge(20);
        long id = jdbcTemplateEnhancement.insertAndReturnGeneratedKey("t_user", user, "id");
        int count = jdbcTemplateEnhancement.queryForObject(Integer.class, "select count(*) from t_user");
        User actual = jdbcTemplateEnhancement.queryForObject(User.class, "select * from t_user where id = " + id);
        assertThat(count, is(1));
        user.setId(id);
        assertThat(actual, is(user));
    }
}
