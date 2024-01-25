package com.njman.ptrnapi.services.impl;

import com.njman.ptrnapi.models.Profile;
import com.njman.ptrnapi.models.User;
import com.njman.ptrnapi.repositories.ProfileRepository;
import com.njman.ptrnapi.repositories.UserRepository;
import com.njman.ptrnapi.services.UserService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public User createUserFromEmail(String email) {
        var user = User
                .builder()
                .email(email)
                .build();

        return userRepository.save(user);

    }

    @Override
    public void isExistingUser(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
    }

    @Override
    public void addProfileToUser(User user, Profile profile) {
        user.setProfile(profile);
        userRepository.save(user);
    }

    @Override
    public void validateUserOwnsProfile(User user, Long profileId) {
        if (!user.getProfile().getId().equals(profileId)) {
            throw new IllegalArgumentException("User does not own this profile");
        }
    }


}
