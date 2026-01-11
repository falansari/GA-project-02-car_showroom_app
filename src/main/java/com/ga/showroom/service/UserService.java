package com.ga.showroom.service;

import com.ga.showroom.exception.InformationExistException;
import com.ga.showroom.model.User;
import com.ga.showroom.model.request.ChangePasswordRequest;
import com.ga.showroom.model.request.LoginRequest;
import com.ga.showroom.model.response.ChangePasswordResponse;
import com.ga.showroom.model.response.LoginResponse;
import com.ga.showroom.repository.UserRepository;
import com.ga.showroom.security.JWTUtils;
import com.ga.showroom.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private MyUserDetails myUserDetails;

    @Autowired
    public UserService(UserRepository userRepository,
                       @Lazy PasswordEncoder passwordEncoder,
                       JWTUtils jwtUtils,
                       @Lazy AuthenticationManager authenticationManager,
                       @Lazy MyUserDetails myUserDetails) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.myUserDetails = myUserDetails;
    }

    public User createUser(User userObject) {
        System.out.println("service calling createUser ==> ");
        if(!userRepository.existsByEmailAddress(userObject.getEmailAddress())){
            userObject.setPassword(passwordEncoder.encode(userObject.getPassword()));
            return userRepository.save(userObject);
        } else {
            throw new InformationExistException("User with email address " + userObject.getUserName() + " already exists.");
        }
    }

    public User findUserByEmailAddress(String email) {
        return userRepository.findUserByEmailAddress(email);
    }

    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword());
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword()));
            System.out.println("authentication :: "+authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            myUserDetails = (MyUserDetails) authentication.getPrincipal();
            System.out.println("myUserDetails :::: "+myUserDetails.getUsername());
            final String JWT = jwtUtils.generateJwtToken(myUserDetails);
            System.out.println("jwtt"+JWT);
            return ResponseEntity.ok(new LoginResponse(JWT));
        } catch (Exception e) {
            return ResponseEntity.ok(new LoginResponse("Error : user name or password is incorrect"));
        }
    }

    /**
     * Get current logged in user
     * @return
     */
    public static User getCurrentLoggedInUser() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assert userDetails != null;
        return userDetails.getUser();
    }

    public ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findUserByEmailAddress(UserService.getCurrentLoggedInUser().getEmailAddress());

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            return new ChangePasswordResponse("Old password incorrect");
        }

        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            return new ChangePasswordResponse("New password cannot be the same as old password");
        } else {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
             userRepository.save(user);
             return new ChangePasswordResponse("Password for " + user.getUserName() + " has been changed successfully!");
        }
    }

}
