package com.njman.ptrnapi.services;

import com.njman.ptrnapi.daos.requests.ChangePasswordRequest;
import com.njman.ptrnapi.daos.requests.SignInRequest;
import com.njman.ptrnapi.daos.requests.SignUpRequest;
import com.njman.ptrnapi.daos.responses.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request);
    JwtAuthenticationResponse signIn(SignInRequest request);
    String changePassword(String email, ChangePasswordRequest request);
}
