package com.ga.showroom.controller;

import com.ga.showroom.model.User;
import com.ga.showroom.model.request.ChangePasswordRequest;
import com.ga.showroom.model.request.LoginRequest;
import com.ga.showroom.model.response.ChangePasswordResponse;
import com.ga.showroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth/users")
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User createUser(@RequestBody User userObject) {
        System.out.println("Calling createUser ==> ");
        return userService.createUser(userObject);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        System.out.println("calling loginUser ==>");
        return userService.loginUser(loginRequest);
    }

    @PutMapping("/changePassword")
    public ChangePasswordResponse changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        System.out.println("calling changePassword ==>");
        return userService.changePassword(changePasswordRequest);
    }

}
