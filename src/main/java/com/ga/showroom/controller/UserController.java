package com.ga.showroom.controller;

import com.ga.showroom.model.User;
import com.ga.showroom.model.UserProfile;
import com.ga.showroom.model.enums.Role;
import com.ga.showroom.model.request.*;
import com.ga.showroom.model.response.ChangePasswordResponse;
import com.ga.showroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping(path = "/updateProfile",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserProfile updateProfile(@RequestPart UserProfile userProfile, @RequestParam("cprImage") MultipartFile cprImage) {
        System.out.println("calling updateProfile ==> ");
        return userService.updateProfile(userProfile, cprImage);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgetPasswordRequest request) {
        userService.forgotPassword(request.getEmailAddress());
        return ResponseEntity.ok("If the email exists, a password reset link has been sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully");
    }

    @PatchMapping("/change-role")
    public User updateUserRole(@RequestParam("email") String userEmail, @RequestParam("role") Role role) {
        return userService.updateUserRole(userEmail, role);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailRequest request) {
        userService.verifyEmail(request.getToken());
        return ResponseEntity.ok("Email has been verified successfully!");
    }
}
