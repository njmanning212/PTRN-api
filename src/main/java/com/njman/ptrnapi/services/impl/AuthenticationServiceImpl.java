package com.njman.ptrnapi.services.impl;

import com.njman.ptrnapi.daos.requests.AdminSignUpRequest;
import com.njman.ptrnapi.daos.requests.ChangePasswordRequest;
import com.njman.ptrnapi.daos.requests.SignInRequest;
import com.njman.ptrnapi.daos.requests.SignUpRequest;
import com.njman.ptrnapi.daos.responses.JwtAuthenticationResponse;
import com.njman.ptrnapi.models.Profile;
import com.njman.ptrnapi.models.Role;
import com.njman.ptrnapi.models.User;
import com.njman.ptrnapi.repositories.ProfileRepository;
import com.njman.ptrnapi.repositories.UserRepository;
import com.njman.ptrnapi.services.AuthenticationService;
import com.njman.ptrnapi.services.JwtService;

import lombok.RequiredArgsConstructor;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Value("${admin.code}")
    private String adminCode;


    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) throws BadRequestException {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match.");
        }

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new BadRequestException("Email already in use.");
        }
        try {
            var user = User
                    .builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            var profile = Profile
                    .builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .user(user)
                    .role(Role.PATIENT)
                    .build();

            user.setProfile(profile);

            userRepository.save(user);
            profileRepository.save(profile);

            var jwt = jwtService.generateToken(user);
            return JwtAuthenticationResponse
                    .builder()
                    .token(jwt)
                    .build();
        }
        catch (Exception e) {
            Optional<User> existingUser2 = userRepository.findByEmail(request.getEmail());
            if (existingUser2.isPresent()) {
                if (existingUser2.get().getProfile() != null)
                    profileRepository.delete(existingUser2.get().getProfile());
                userRepository.delete(existingUser2.get());
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) throws BadRequestException {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            throw new BadRequestException("Invalid password.");
        }

        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse adminSignUp(AdminSignUpRequest request) {
        if (!request.getAdminCode().equals(adminCode)) {
            throw new RuntimeException("Invalid admin code.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        try {
            var user = User
                    .builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            var profile = Profile
                    .builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .role(Role.ADMIN)
                    .user(user)
                    .build();

            user.setProfile(profile);

            userRepository.save(user);
            profileRepository.save(profile);

            var jwt = jwtService.generateToken(user);
            return JwtAuthenticationResponse
                    .builder()
                    .token(jwt)
                    .build();

        } catch (Exception e) {
            Optional<User> existingUser2 = userRepository.findByEmail(request.getEmail());
            if (existingUser2.isPresent()) {
                profileRepository.delete(existingUser2.get().getProfile());
                userRepository.delete(existingUser2.get());
            }
            throw new RuntimeException("Error signing up admin.");
        }

    }

    @Override
    public String changePassword(String email, ChangePasswordRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getOldPassword()));
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        if (request.getNewPassword().equals(request.getOldPassword()))
            return "New password cannot be the same as old password.";
        if (!request.getNewPassword().equals(request.getNewPasswordConfirm()))
            return "New passwords do not match.";
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Password changed successfully.";
    }
}