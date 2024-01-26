package com.njman.ptrnapi.models;

public enum Role {
    ADMIN(500, "Admin"),
    CLINIC_ADMIN(400, "Clinic Admin"),
    THERAPIST(300, "Therapist"),
    RECEPTIONIST(200,  "Receptionist"),
    PATIENT(100, "Patient");

    private final int value;
    private final String name;

    private Role(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {return name;}
}
