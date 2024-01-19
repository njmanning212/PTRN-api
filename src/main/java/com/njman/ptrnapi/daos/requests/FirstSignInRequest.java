package com.njman.ptrnapi.daos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirstSignInRequest {
    private String email;
    private String password;
    private String confirmPassword;
}
