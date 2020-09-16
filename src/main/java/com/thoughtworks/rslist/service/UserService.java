package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    private final List<User> userList = new ArrayList<>();

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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

    public User getUser(int index) {
        return userList.get(index);
    }

    public int addUser(User user) {
        if (!isExistByName(user.getUserName())) {
            userList.add(user);
            return userList.size() - 1;
        }
        return -1;
    }

    public boolean contains(User user) {
        return userList.contains(user);
    }

    public void init(List<User> users) {
        userList.clear();
        userList.addAll(users);
    }
}
