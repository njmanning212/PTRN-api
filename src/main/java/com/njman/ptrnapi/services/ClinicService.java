package com.njman.ptrnapi.services;

import com.njman.ptrnapi.daos.requests.CreateClinicRequest;
import com.njman.ptrnapi.daos.responses.CreateClinicResponse;
import com.njman.ptrnapi.models.User;

public interface ClinicService {
    CreateClinicResponse createClinic(User user, CreateClinicRequest request);
}
