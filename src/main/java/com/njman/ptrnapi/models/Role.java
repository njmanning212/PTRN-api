package com.njman.ptrnapi.models;

public enum Role {
    ADMIN(500),
    CLINIC_ADMIN(400),
    THERAPIST(300),
    RECEPTIONIST(200),
    PATIENT(100);

    private final int value;

    private Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
