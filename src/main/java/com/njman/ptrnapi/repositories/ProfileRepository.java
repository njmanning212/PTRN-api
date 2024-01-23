package com.njman.ptrnapi.repositories;

import com.njman.ptrnapi.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByClinicId(Long clinicId);
}
