package com.njman.ptrnapi.services.impl;

import com.njman.ptrnapi.daos.requests.CreateProfileRequest;
import com.njman.ptrnapi.daos.responses.ProfileResponse;
import com.njman.ptrnapi.exceptions.AuthorizationDeniedException;
import com.njman.ptrnapi.models.Profile;
import com.njman.ptrnapi.models.Role;
import com.njman.ptrnapi.models.User;
import com.njman.ptrnapi.repositories.ProfileRepository;
import com.njman.ptrnapi.repositories.UserRepository;
import com.njman.ptrnapi.services.ClinicService;
import com.njman.ptrnapi.services.CloudinaryImageService;
import com.njman.ptrnapi.services.ProfileService;
import com.njman.ptrnapi.services.UserService;
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
    private final UserService userService;
    private final ClinicService clinicService;

    @Override
    @Transactional
    public ProfileResponse createProfile(User user, CreateProfileRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role userRole = user.getProfile().getRole();
        Role newProfileRole = determineRole(request.getRoleString());

        if (userRole.getValue() < newProfileRole.getValue()) {
            throw new AuthorizationDeniedException("User does not have permission to create this profile");
        }

        var newUser = userService.createUserFromEmail(request.getEmail());

        var newProfile = Profile
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(newProfileRole)
                .user(newUser)
                .build();

        if (newProfileRole.getValue() < 500 ) {
            if (userRole.getValue() < 500) {
                newProfile.setClinic(user.getProfile().getClinic());
            }
            newProfile.setClinic(clinicService.getClinicEntityById(request.getClinicId()));
        }

        profileRepository.save(newProfile);

        userService.addProfileToUser(newUser, newProfile);

        return ProfileResponse
                .builder()
                .id(newProfile.getId())
                .firstName(newProfile.getFirstName())
                .email(request.getEmail())
                .lastName(newProfile.getLastName())
                .roleString(request.getRoleString())
                .roleValue(newProfile.getRole().getValue())
                .build();
    }

    @Override
    public Role determineRole(String roleString) {
        return switch (roleString) {
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
                .profilePhotoURL(profile.getProfilePhotoURL())
                .build();

        if (profile.getClinic() != null) {
            profileResponse.setClinicId(profile.getClinic().getId());
        }

        return profileResponse;
    }
}
