package com.njman.ptrnapi.services;

import com.njman.ptrnapi.daos.requests.*;
import com.njman.ptrnapi.daos.responses.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request);
    JwtAuthenticationResponse login(LoginRequest request);
    JwtAuthenticationResponse firstSignIn(FirstSignInRequest request);
    void changePassword(String email, ChangePasswordRequest request);
}
