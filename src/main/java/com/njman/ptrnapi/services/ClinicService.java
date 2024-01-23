package com.njman.ptrnapi.services;

import com.njman.ptrnapi.daos.requests.CreateClinicRequest;
import com.njman.ptrnapi.daos.responses.ClinicResponse;
import com.njman.ptrnapi.daos.responses.CreateClinicResponse;
import com.njman.ptrnapi.models.Clinic;
import com.njman.ptrnapi.models.User;

import java.util.List;

public interface ClinicService {
    CreateClinicResponse createClinic(User user, CreateClinicRequest request);
    List<Clinic> getAllClinics(User user);
    ClinicResponse getClinicById(Long id);
}
