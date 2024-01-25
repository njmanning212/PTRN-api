package com.njman.ptrnapi.models;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_profile")
public class Profile {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String profilePhotoURL;
    private Date createdAt;
    private Date updatedAt;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = true)
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

}

