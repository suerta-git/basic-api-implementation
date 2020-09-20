package com.thoughtworks.rslist.po;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPO {
    @Override
    public String toString() {
        return "UserPO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", voteNum=" + voteNum +
                '}';
    }

    public UserPO(String userName, int age, String gender, String email, String phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    @Id @GeneratedValue
    private int id;
    private String userName;
    private int age;
    private String gender;
    private String email;
    private String phone;
    private int voteNum = 10;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userPO")
    private List<RsEventPO> rsEventPOs;

    public void vote(Integer voteNum) {
        this.voteNum -= voteNum;
    }
}
