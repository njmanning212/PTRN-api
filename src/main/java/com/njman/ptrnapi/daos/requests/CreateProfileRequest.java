package com.njman.ptrnapi.daos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProfileRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String stringRole;
    private Long clinicId;
}
