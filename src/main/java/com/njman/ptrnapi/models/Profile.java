package com.njman.ptrnapi.models;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Enumerated(EnumType.STRING)
    private Role role;
    private String profilePhotoURL;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    User user;


}

