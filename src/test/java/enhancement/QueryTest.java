package enhancement;

import base.AbstractIntegrationTest;
import model.User;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

        executeNativeSql("insert into t_user(id, name, age) values(1, 'zhang shen', 30)");
        count = jdbcTemplateEnhancement.queryForObject(Integer.class, "select count(*) from t_user");
        assertThat(count, is(1));

        long longCount = jdbcTemplateEnhancement.queryForObject(Long.class, "select count(*) from t_user");
        assertThat(longCount, is(1L));
    }

    @Test
    public void shouldQueryForObjectCorrectly() throws Exception {
        executeNativeSql("insert into t_user(id, name, age) values(1, 'zhang shen', 30)");

        User user = jdbcTemplateEnhancement.queryForObject(User.class, "select * from t_user");
        assertNotNull(user);
        assertThat(user.getId(), is(1L));
        assertThat(user.getAge(), is(30));
        assertThat(user.getName(), is("zhang shen"));
        assertNull(user.getNullValue());

        executeNativeSql("insert into t_user values(2, 'bruce', 29)");
        user = jdbcTemplateEnhancement.queryForObject(User.class, "select * from t_user where id=?", 2);
        assertNotNull(user);
        assertThat(user.getId(), is(2L));
    }

    @Test
    public void shouldRetrieveDateCorrectlly() throws Exception {
        executeNativeSql("insert into t_user(id, name, age, dob) values(1, 'zhang shen', 30, '2015-2-11')");
        User user = jdbcTemplateEnhancement.queryForObject(User.class, "select * from t_user");
        assertThat(formatDate(user.getDob()), is("2015-02-11"));
    }

    @Test
    public void shouldRetrieveCalendarCorrectlly() throws Exception {
        executeNativeSql("insert into t_user(id, name, age, dob) values(1, 'zhang shen', 30, '2015-2-11')");
        Calendar calendar = jdbcTemplateEnhancement.queryForObject(Calendar.class, "select dob from t_user");
        assertThat(formatDate(calendar.getTime()), is("2015-02-11"));
    }

    @Test
    public void shouldRetrieveStringCorrectlly() throws Exception {
        executeNativeSql("insert into t_user(id, name, age) values(1, 'zhang shen', 30)");
        String name = jdbcTemplateEnhancement.queryForObject(String.class, "select name from t_user");
        assertThat(name, is("zhang shen"));
    }

    @Test
    public void shouldRetrieveList() throws Exception {
        executeNativeSql("insert into t_user(id, name, age) values(1, 'zhang shen', 30)");
        executeNativeSql("insert into t_user(id, name, age) values(2, 'zhang shen', 30)");
        executeNativeSql("insert into t_user(id, name, age) values(3, 'zhang shen', 30)");

        List<User> users = jdbcTemplateEnhancement.queryForList(User.class, "select * from t_user");
        assertThat(users.size(), is(3));
    }

    @Test
    public void shouldRetrieveDateTimeCorrectly() throws Exception {
        executeNativeSql("insert into t_user(id, name, age, dob) values(1, 'zhang shen', 30, '2015-2-11 10:10:10')");
        Date date = jdbcTemplateEnhancement.queryForObject(Date.class, "select dob from t_user where id = 1");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assertThat(format.format(date), is("2015-02-11 10:10:10"));
    }
}
