package com.njman.ptrnapi.services.impl;

import com.njman.ptrnapi.daos.requests.*;
import com.njman.ptrnapi.daos.responses.JwtAuthenticationResponse;
import com.njman.ptrnapi.models.Profile;
import com.njman.ptrnapi.models.Role;
import com.njman.ptrnapi.models.User;
import com.njman.ptrnapi.repositories.ProfileRepository;
import com.njman.ptrnapi.repositories.UserRepository;
import com.njman.ptrnapi.services.AuthenticationService;
import com.njman.ptrnapi.services.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.NoSuchElementException;
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
    @Transactional
    public JwtAuthenticationResponse signUp(SignUpRequest request){

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        Date now = new Date();

        var user = User
                .builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        var profile = Profile
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .createdAt(now)
                .updatedAt(now)
                .user(user)
                .build();

        if (request.getIsAdmin()) {
            validateAdminCode(request.getAdminCode());
            profile.setRole(Role.ADMIN);
        }
        else {
            profile.setRole(Role.PATIENT);
        }

        user.setProfile(profile);

        userRepository.save(user);
        profileRepository.save(profile);

        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse
                .builder()
                .token(jwt)
                .build();
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request){
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    @Transactional
    public JwtAuthenticationResponse adminSignUp(AdminSignUpRequest request) {
        if (!request.getAdminCode().equals(adminCode)) {
            throw new IllegalArgumentException("Invalid admin code.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already in use.");
        }

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

    }

    @Override
    public JwtAuthenticationResponse firstSignIn(FirstSignInRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse
                .builder()
                .token(jwt)
                .build();
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request){
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getOldPassword()));

        if (request.getNewPassword().equals(request.getOldPassword()))
            throw new IllegalArgumentException("New password cannot be the same as the old password.");

        if (!request.getNewPassword().equals(request.getNewPasswordConfirm()))
            throw new IllegalArgumentException("New passwords do not match.");

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private void validateAdminCode(String adminCode) {
        if (!adminCode.equals(this.adminCode)) {
            throw new IllegalArgumentException("Invalid admin code.");
        }
    }
}