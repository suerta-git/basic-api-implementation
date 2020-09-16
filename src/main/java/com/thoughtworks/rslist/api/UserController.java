package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public List<User> getUserList() {
        return userService.getUserList();
    }

    @PostMapping("/user")
    public void addUser(@RequestBody @Valid User user){
        userService.addUser(user);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<Error> exceptionHandler(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(new Error("invalid user"));
    }
}
