package com.njman.ptrnapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_clinic")
public class Clinic {
    @Id
    @GeneratedValue
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

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL)
    private List<Profile> profiles;
}
