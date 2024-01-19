package com.njman.ptrnapi.services.impl;

import com.njman.ptrnapi.daos.requests.CreateProfileRequest;
import com.njman.ptrnapi.daos.responses.ProfileResponse;
import com.njman.ptrnapi.models.Profile;
import com.njman.ptrnapi.models.Role;
import com.njman.ptrnapi.models.User;
import com.njman.ptrnapi.repositories.ProfileRepository;
import com.njman.ptrnapi.repositories.UserRepository;
import com.njman.ptrnapi.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProfileResponse createProfile(CreateProfileRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role role = determineRole(request.getStringRole());

        var user = User
                .builder()
                .email(request.getEmail())
                .build();

        var profile = Profile
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(role)
                .user(user)
                .build();

        user.setProfile(profile);

        userRepository.save(user);
        profileRepository.save(profile);

        return ProfileResponse
                .builder()
                .id(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .role(profile.getRole())
                .build();
    }

    @Override
    public Role determineRole(String stringRole) {
        return switch (stringRole) {
            case "Admin" -> Role.ADMIN;
            case "Clinic Admin" -> Role.CLINIC_ADMIN;
            case "Therapist" -> Role.THERAPIST;
            case "PATIENT" -> Role.PATIENT;
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }
}
