package com.njman.ptrnapi.services;

import com.njman.ptrnapi.daos.requests.CreateProfileRequest;
import com.njman.ptrnapi.daos.responses.ProfileResponse;
import com.njman.ptrnapi.models.Role;
import com.njman.ptrnapi.models.User;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    ProfileResponse createProfile(CreateProfileRequest request);
    Role determineRole(String stringRole);
    ProfileResponse addProfilePhoto(User user, Long id, MultipartFile image);
}
