package com.njman.ptrnapi.services;

import com.njman.ptrnapi.daos.requests.AdminSignUpRequest;
import com.njman.ptrnapi.daos.requests.ChangePasswordRequest;
import com.njman.ptrnapi.daos.requests.SignInRequest;
import com.njman.ptrnapi.daos.requests.SignUpRequest;
import com.njman.ptrnapi.daos.responses.JwtAuthenticationResponse;
import org.apache.coyote.BadRequestException;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request) throws BadRequestException;
    JwtAuthenticationResponse signIn(SignInRequest request) throws BadRequestException;
    JwtAuthenticationResponse adminSignUp(AdminSignUpRequest request) throws BadRequestException;
    void changePassword(String email, ChangePasswordRequest request) throws BadRequestException;
}
