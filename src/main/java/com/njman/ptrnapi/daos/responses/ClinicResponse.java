package com.njman.ptrnapi.daos.responses;

import com.njman.ptrnapi.models.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicResponse {
    private Long id;
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String einNumber;
    private Date createdAt;
    private Date updatedAt;
    private List<Profile> profiles;
}
