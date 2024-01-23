package com.njman.ptrnapi.controllers;

import com.njman.ptrnapi.daos.requests.CreateClinicRequest;
import com.njman.ptrnapi.daos.responses.CreateClinicResponse;
import com.njman.ptrnapi.models.User;
import com.njman.ptrnapi.services.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ptrn/api/clinics")
@RequiredArgsConstructor
public class ClinicController {
    private final ClinicService clinicService;

    @PostMapping()
    public CreateClinicResponse createClinic(@AuthenticationPrincipal User user, @RequestBody CreateClinicRequest request) {
        return clinicService.createClinic(user, request);
    }
}
