package com.njman.ptrnapi.services;

import com.njman.ptrnapi.models.Profile;
import com.njman.ptrnapi.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    User createUserFromEmail(String email);
    void isExistingUser(String email);
    void addProfileToUser(User user, Profile profile);
    void validateUserOwnsProfile(User user, Long profileId);

}
