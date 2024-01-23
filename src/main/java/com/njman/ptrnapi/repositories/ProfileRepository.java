package com.njman.ptrnapi.repositories;

import com.njman.ptrnapi.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
