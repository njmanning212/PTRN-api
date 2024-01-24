package com.njman.ptrnapi.services.impl;

import com.njman.ptrnapi.daos.requests.CreateProfileRequest;
import com.njman.ptrnapi.daos.responses.ProfileResponse;
import com.njman.ptrnapi.models.Profile;
import com.njman.ptrnapi.models.Role;
import com.njman.ptrnapi.models.User;
import com.njman.ptrnapi.repositories.ProfileRepository;
import com.njman.ptrnapi.repositories.UserRepository;
import com.njman.ptrnapi.services.CloudinaryImageService;
import com.njman.ptrnapi.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final CloudinaryImageService cloudinaryImageService;

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

    @Override
    public ProfileResponse addProfilePhoto(User user, Long id, MultipartFile image) {
        if (!user.getProfile().getId().equals(id)) {
            throw new IllegalArgumentException("User does not own this profile");
        }

        var profile = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        var imageUrl = cloudinaryImageService.uploadImage(image);

        profile.setProfilePhotoURL(imageUrl);
        profileRepository.save(profile);

        return ProfileResponse
                .builder()
                .id(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .role(profile.getRole())
                .profilePhotoURL(profile.getProfilePhotoURL())
                .build();
    }

    @Override
    public List<Profile> getProfilesByClinicId(Long clinicId) {
        return profileRepository.findByClinicId(clinicId);
    }

    public ProfileResponse getProfileById(Long id) {
        Optional<Profile> existingProfile =  profileRepository.findById(id);
        if (existingProfile.isEmpty()) {
            throw new NoSuchElementException("Profile not found");
        }
        var profile = existingProfile.get();

        var profileResponse =  ProfileResponse
                .builder()
                .id(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .role(profile.getRole())
                .profilePhotoURL(profile.getProfilePhotoURL())
                .build();

        if (profile.getClinic() != null) {
            profileResponse.setClinicId(profile.getClinic().getId());
        }

        return profileResponse;
    }
}
