package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class UserController {
    private final List<User> userList = new ArrayList<>(Arrays.asList(
            new User("user1", 20, "male", "user1@test.com", "18888888888"),
            new User("user2", 20, "female", "user2@test.com", "18888888888"),
            new User("user3", 20, "female", "user3@test.com", "18888888888")
    ));

    @GetMapping("/user")
    public List<User> getUserList() {
        return userList;
    }

    @PostMapping("/user")
    public void addUser(@RequestBody @Valid User user){
        userList.add(user);
    }
}
