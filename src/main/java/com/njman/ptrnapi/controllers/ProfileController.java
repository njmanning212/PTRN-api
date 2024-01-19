package com.njman.ptrnapi.controllers;

import com.njman.ptrnapi.daos.requests.CreateProfileRequest;
import com.njman.ptrnapi.daos.responses.ProfileResponse;
import com.njman.ptrnapi.exceptions.AuthorizationDeniedException;
import com.njman.ptrnapi.models.User;
import com.njman.ptrnapi.services.ProfileService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/ptrn/api/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse createProfile(@AuthenticationPrincipal User user, @RequestBody CreateProfileRequest request) {
        try {
            if (user.getProfile().getRole().getValue() < profileService.determineRole(request.getStringRole()).getValue()) {
                throw new AuthorizationDeniedException("You are not authorized to create this user!");
            }

            return profileService.createProfile(request);
        }
        catch (AuthorizationDeniedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
