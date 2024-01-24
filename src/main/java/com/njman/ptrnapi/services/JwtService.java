package com.njman.ptrnapi.services;

import com.njman.ptrnapi.models.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token);
    String generateToken(User user);
    boolean isTokenValid(String token, UserDetails userDetails);
}
