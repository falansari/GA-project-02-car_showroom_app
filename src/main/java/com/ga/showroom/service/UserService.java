package com.ga.showroom.service;

import com.ga.showroom.exception.InformationExistException;
import com.ga.showroom.model.PasswordResetToken;
import com.ga.showroom.model.User;
import com.ga.showroom.model.UserProfile;
import com.ga.showroom.model.request.ChangePasswordRequest;
import com.ga.showroom.model.request.LoginRequest;
import com.ga.showroom.model.response.ChangePasswordResponse;
import com.ga.showroom.model.response.LoginResponse;
import com.ga.showroom.repository.PasswordResetTokenRepository;
import com.ga.showroom.repository.UserRepository;
import com.ga.showroom.security.JWTUtils;
import com.ga.showroom.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private MyUserDetails myUserDetails;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private JavaMailSender mailSender;

    @Autowired
    public UserService(UserRepository userRepository,
                       @Lazy PasswordEncoder passwordEncoder,
                       JWTUtils jwtUtils,
                       @Lazy AuthenticationManager authenticationManager,
                       @Lazy MyUserDetails myUserDetails,
                       PasswordResetTokenRepository passwordResetTokenRepository,
                       @Lazy JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.myUserDetails = myUserDetails;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.mailSender = mailSender;
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

    public UserProfile updateProfile(UserProfile userProfile, MultipartFile cprImage) {
        User user = userRepository.findUserByEmailAddress(UserService.getCurrentLoggedInUser().getEmailAddress());

        UserProfile profile = user.getUserProfile();

        profile.setFirstName(userProfile.getFirstName());
        profile.setLastName(userProfile.getLastName());
        profile.setEmailAddress(userProfile.getEmailAddress());
        profile.setPhoneNumber(userProfile.getPhoneNumber());
        profile.setHomeAddress(userProfile.getHomeAddress());
        profile.setCpr(userProfile.getCpr());

        // handle CPR image upload
        if (cprImage != null && !cprImage.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + cprImage.getOriginalFilename();

                // better upload location (outside classpath)
                Path uploadPath = Paths.get("uploads/cpr-images");
                Files.createDirectories(uploadPath);

                Files.copy(
                        cprImage.getInputStream(),
                        uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING
                );

                // save filename in DB
                profile.setCprImage(fileName);

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload CPR image", e);
            }
        }

        userRepository.save(user);
        return profile;
    }

    @Transactional
    public void forgotPassword(String emailAddress) {
        User user = userRepository.findUserByEmailAddress(emailAddress);
        if (user == null) {
            return;
        }

        passwordResetTokenRepository.deleteByUser(user);
        passwordResetTokenRepository.flush();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        passwordResetTokenRepository.save(resetToken);

        sendResetEmail(user.getEmailAddress(), resetToken.getToken());
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }

    private void sendResetEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);

        message.setFrom("no-reply@showroom.com");

        message.setSubject("Password Reset Request");
        message.setText("Reset your password using this token:\n" + token);

        mailSender.send(message);
    }

}
