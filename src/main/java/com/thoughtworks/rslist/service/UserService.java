package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    private final List<User> userList = new ArrayList<>(Arrays.asList(
            new User("user1", 20, "male", "user1@test.com", "18888888888"),
            new User("user2", 20, "female", "user2@test.com", "18888888888"),
            new User("user3", 20, "female", "user3@test.com", "18888888888")
    ));

    public boolean isExistByName(String userName) {
        return userList.stream().anyMatch((user -> user.getUserName().equals(userName)));
    }

    public List<User> getUserList() {
        return userList;
    }

    public User getUser(String userName) {
        return userList.stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
    }

    public void addUser(User user) {
        if (!isExistByName(user.getUserName())) {
            userList.add(user);
        }
    }

    public boolean contains(User user) {
        return userList.contains(user);
    }
}
