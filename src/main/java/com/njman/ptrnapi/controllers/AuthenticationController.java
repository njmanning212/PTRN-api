package com.njman.ptrnapi.controllers;

import com.njman.ptrnapi.daos.requests.AdminSignUpRequest;
import com.njman.ptrnapi.daos.requests.ChangePasswordRequest;
import com.njman.ptrnapi.daos.requests.SignInRequest;
import com.njman.ptrnapi.daos.requests.SignUpRequest;
import com.njman.ptrnapi.daos.responses.ErrorResponse;
import com.njman.ptrnapi.daos.responses.JwtAuthenticationResponse;
import com.njman.ptrnapi.models.User;
import com.njman.ptrnapi.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ptrn/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @Value("${admin.code}")
    private String adminCode;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.signUp(request));
        } catch (Exception e) {
            new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal User user, @RequestBody ChangePasswordRequest request) {
        var email = user.getEmail();
        return ResponseEntity.ok(authenticationService.changePassword(email, request));
    }

    @PostMapping("signup/admin")
    public ResponseEntity<JwtAuthenticationResponse> adminSignUp(@RequestBody AdminSignUpRequest request) {
        if (!request.getAdminCode().equals(adminCode)) {
            return ResponseEntity.badRequest().build();
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(authenticationService.adminSignUp(request));
    }

}
