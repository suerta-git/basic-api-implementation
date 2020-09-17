package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final List<User> userList = new ArrayList<>();

    @Autowired private UserRepository userRepository;

    public boolean isExistByName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public List<User> getUserList() {
        return userRepository.findAll().stream()
                .map(userPO -> new User(
                        userPO.getUserName(),
                        userPO.getAge(),
                        userPO.getGender(),
                        userPO.getEmail(),
                        userPO.getPhone()))
                .collect(Collectors.toList());
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
            UserPO userPO = new UserPO(
                    user.getUserName(),
                    user.getAge(),
                    user.getGender(),
                    user.getEmail(),
                    user.getPhone());
            userRepository.save(userPO);
            return userPO.getId();
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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isExistById(int userId) {
        return userId > 0 && userId <= userList.size();
    }
}
