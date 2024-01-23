package com.njman.ptrnapi.controllers;

import com.njman.ptrnapi.daos.requests.CreateClinicRequest;
import com.njman.ptrnapi.daos.responses.ClinicResponse;
import com.njman.ptrnapi.daos.responses.CreateClinicResponse;
import com.njman.ptrnapi.exceptions.AuthorizationDeniedException;
import com.njman.ptrnapi.models.Clinic;
import com.njman.ptrnapi.models.User;
import com.njman.ptrnapi.services.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("ptrn/api/clinics")
@RequiredArgsConstructor
public class ClinicController {
    private final ClinicService clinicService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CreateClinicResponse createClinic(@AuthenticationPrincipal User user, @RequestBody CreateClinicRequest request) {
        try {
            return clinicService.createClinic(user, request);
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

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Clinic> getAllClinics(@AuthenticationPrincipal User user) {
        try {
            return clinicService.getAllClinics(user);
        }
        catch (AuthorizationDeniedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClinicResponse getClinicById(@PathVariable Long id) {
        return clinicService.getClinicById(id);
    }
}
