package com.njman.ptrnapi.repositories;

import com.njman.ptrnapi.models.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Optional<Clinic> findByEinNumber(String einNumber);
    Optional<Clinic> findByName(String name);
}
