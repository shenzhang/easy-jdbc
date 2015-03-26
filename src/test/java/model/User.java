package model;

import com.google.common.base.Objects;

import java.util.Date;

/**
 * User: Zhang Shen
 * Date: 3/23/15
 * Time: 2:32 PM
 */
public class User {
    private long id = 1;
    private String name;
    private int age;
    private Date dob;
    private Integer nullValue;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(User.class)) {
            return false;
        }

        User user = (User)obj;
        return Objects.equal(id, user.getId()) && Objects.equal(name, user.getName()) && Objects.equal(age, user.getAge())
                && Objects.equal(dob, user.getDob()) && Objects.equal(nullValue, user.getNullValue());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Integer getNullValue() {
        return nullValue;
    }

    public void setNullValue(Integer nullValue) {
        this.nullValue = nullValue;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
