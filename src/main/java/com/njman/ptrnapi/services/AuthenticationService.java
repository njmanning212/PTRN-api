package com.njman.ptrnapi.services;

import com.njman.ptrnapi.daos.requests.*;
import com.njman.ptrnapi.daos.responses.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request);
    JwtAuthenticationResponse signIn(SignInRequest request);
    JwtAuthenticationResponse adminSignUp(AdminSignUpRequest request);
    JwtAuthenticationResponse firstSignIn(FirstSignInRequest request);
    void changePassword(String email, ChangePasswordRequest request);
}
