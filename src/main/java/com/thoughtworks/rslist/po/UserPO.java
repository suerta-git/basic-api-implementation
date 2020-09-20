package com.thoughtworks.rslist.po;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userPO")
    private List<RsEventPO> rsEventPOs;

    public void vote(Integer voteNum) {
        this.voteNum -= voteNum;
    }
}
