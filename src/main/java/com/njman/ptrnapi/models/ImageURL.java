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
@Table(name = "_image")
public class ImageURL {
    @Id
    @GeneratedValue
    private Long id;
    private String url;

    @OneToOne(optional = true)
    @JoinColumn(name = "profile_id")
    Profile profile;
}
