package com.thoughtworks.rslist.domain;

import javax.validation.constraints.*;
import java.util.Objects;

public class User {
    @NotNull @Size(max = 8)
    private String userName;

    @Min(18) @Max(100)
    private int age;

    @NotNull
    private String gender;

    @Email
    private String email;

    @Pattern(regexp = "1\\d{10}")
    private String phone;

    public User(String userName, int age, String gender, String email, String phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(gender, user.gender) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phone, user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, age, gender, email, phone);
    }
}
