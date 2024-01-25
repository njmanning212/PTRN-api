package com.njman.ptrnapi.controllers;

import com.njman.ptrnapi.daos.requests.CreateProfileRequest;
import com.njman.ptrnapi.daos.responses.ProfileResponse;
import com.njman.ptrnapi.exceptions.AuthorizationDeniedException;
import com.njman.ptrnapi.models.Profile;
import com.njman.ptrnapi.models.User;
import com.njman.ptrnapi.services.ProfileService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/ptrn/api/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse createProfile(@AuthenticationPrincipal User user, @RequestBody CreateProfileRequest request) {
        try {
            return profileService.createProfile(user, request);
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

    @PutMapping("/{id}/add-photo")
    public ProfileResponse addProfilePicture(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestParam("photo") MultipartFile photo) {
        try {
            return profileService.addProfilePhoto(user, id, photo);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse getProfileById(@PathVariable Long id) {
        try {
            return profileService.getProfileById(id);
        }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
