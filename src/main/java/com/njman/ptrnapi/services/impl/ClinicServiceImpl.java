package com.njman.ptrnapi.services.impl;

import com.njman.ptrnapi.daos.requests.CreateClinicRequest;
import com.njman.ptrnapi.daos.responses.ClinicResponse;
import com.njman.ptrnapi.daos.responses.CreateClinicResponse;
import com.njman.ptrnapi.exceptions.AuthorizationDeniedException;
import com.njman.ptrnapi.models.Clinic;
import com.njman.ptrnapi.models.Profile;
import com.njman.ptrnapi.models.User;
import com.njman.ptrnapi.repositories.ClinicRepository;
import com.njman.ptrnapi.services.ClinicService;
import com.njman.ptrnapi.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {
    private final ClinicRepository clinicRepository;
    private final ProfileService profileService;
    @Override
    public CreateClinicResponse createClinic(User user, CreateClinicRequest request) {
        if (user.getProfile().getRole().getValue() != 500) {
            throw new AuthorizationDeniedException("Must be an Admin to create a clinic!");
        }

        Optional<Clinic> existingEinNumber = clinicRepository.findByEinNumber(request.getEinNumber());
        Optional<Clinic> existingName = clinicRepository.findByName(request.getName());

        if (existingEinNumber.isPresent()) {
            throw new IllegalArgumentException("Clinic with Ein Number already exists!");
        }

        if (existingName.isPresent()) {
            throw new IllegalArgumentException("Clinic with Name already exists!");
        }

        Date now = new Date();

        var clinic = Clinic
                .builder()
                .name(request.getName())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .einNumber(request.getEinNumber())
                .createdAt(now)
                .updatedAt(now)
                .build();

        clinicRepository.save(clinic);

        return CreateClinicResponse
                .builder()
                .id(clinic.getId())
                .name(clinic.getName())
                .addressLine1(clinic.getAddressLine1())
                .addressLine2(clinic.getAddressLine2())
                .city(clinic.getCity())
                .state(clinic.getState())
                .postalCode(clinic.getPostalCode())
                .einNumber(clinic.getEinNumber())
                .createdAt(clinic.getCreatedAt())
                .updatedAt(clinic.getUpdatedAt())
                .build();

    }

    @Override
    public List<Clinic> getAllClinics(User user) {
        if (user.getProfile().getRole().getValue() != 500) {
            throw new AuthorizationDeniedException("Must be an Admin to view all clinics!");
        }
        return clinicRepository.findAllByOrderByNameAsc();
    }

    @Override
    public ClinicResponse getClinicById(Long id) {

        Optional<Clinic> existingClinic = clinicRepository.findById(id);

        if (existingClinic.isEmpty()) {
            throw new NoSuchElementException("Clinic with id " + id + " does not exist!");
        }

        Clinic clinic = existingClinic.get();
        List<Profile> profiles = profileService.getProfilesByClinicId(id);

        return ClinicResponse
                .builder()
                .id(clinic.getId())
                .name(clinic.getName())
                .addressLine1(clinic.getAddressLine1())
                .addressLine2(clinic.getAddressLine2())
                .city(clinic.getCity())
                .state(clinic.getState())
                .postalCode(clinic.getPostalCode())
                .einNumber(clinic.getEinNumber())
                .createdAt(clinic.getCreatedAt())
                .updatedAt(clinic.getUpdatedAt())
                .profiles(profiles)
                .build();
    }
}
