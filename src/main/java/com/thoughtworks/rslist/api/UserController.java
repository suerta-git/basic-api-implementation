package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.bo.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.UserNotValidException;
import com.thoughtworks.rslist.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUserList() {
        return ResponseEntity.ok(userService.getUserList());
    }

    @PostMapping("/user")
    public ResponseEntity<Void> addUser(@RequestBody @Valid User user){
        int userId = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).header("id", String.valueOf(userId)).build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable int userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    private ResponseEntity<Error> exceptionHandler(MethodArgumentNotValidException e) {
        logger.error("Here is a invalid user");
        return ResponseEntity.badRequest().body(new Error("invalid user"));
    }


    @ExceptionHandler(UserNotValidException.class)
    private ResponseEntity<Error> exceptionHandler(UserNotValidException e){
        logger.error("Here is a " + e.getMessage());
        return ResponseEntity.badRequest().body(new Error(e.getMessage()));
    }
}
