package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.UserNotValidException;
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

    public User getUser(int userId) {
        return toUser(userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotValidException("invalid user id")));
    }

    public int addUser(User user) {
        if (!isExistByName(user.getUserName())) {
            UserPO userPO = toUserPO(user);
            userRepository.save(userPO);
            return userPO.getId();
        }
        return -1;
    }

    public boolean isExist(User user) {
        return userRepository.existsByUserName(user.getUserName());
    }

    private UserPO toUserPO(User user) {
        return new UserPO(
                user.getUserName(),
                user.getAge(),
                user.getGender(),
                user.getEmail(),
                user.getPhone());
    }

    private User toUser(UserPO userPO) {
        return new User(
                userPO.getUserName(),
                userPO.getAge(),
                userPO.getGender(),
                userPO.getEmail(),
                userPO.getPhone());
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isExistById(int userId) {
        return userRepository.existsById(userId);
    }

    public void deleteUser(int userId) {
        if (!isExistById(userId)) {
            throw new UserNotValidException("invalid user id");
        }
        userRepository.deleteById(userId);
    }
}
