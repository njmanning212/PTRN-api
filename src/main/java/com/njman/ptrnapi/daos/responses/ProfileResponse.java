package com.njman.ptrnapi.daos.responses;

import com.njman.ptrnapi.models.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String roleName;
    private Integer roleValue;
    private String profilePhotoURL;
    private Long clinicId;
    private Date createdAt;
    private Date updatedAt;
}
