package com.njman.ptrnapi.services.impl;

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

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
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

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse
                .builder()
                .token(jwt)
                .build();
    }

    @Override
    public void changePassword(String email, ChangePasswordRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getOldPassword()));
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        if (request.getNewPassword().equals(request.getOldPassword()))
            throw new IllegalArgumentException("New password cannot be the same as old password.");
        if (!request.getNewPassword().equals(request.getNewPasswordConfirm()))
            throw new IllegalArgumentException("New password and new password confirmation do not match.");
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    }
}