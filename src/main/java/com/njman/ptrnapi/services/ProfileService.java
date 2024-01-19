package com.njman.ptrnapi.services;

import com.njman.ptrnapi.daos.requests.CreateProfileRequest;
import com.njman.ptrnapi.daos.responses.ProfileResponse;
import com.njman.ptrnapi.models.Role;

public interface ProfileService {
    ProfileResponse createProfile(CreateProfileRequest request);
    Role determineRole(String stringRole);
}
