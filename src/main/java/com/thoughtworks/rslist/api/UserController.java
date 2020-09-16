package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

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
        logger.error("Here is a invalid user");
        return ResponseEntity.badRequest().body(new Error("invalid user"));
    }
}
