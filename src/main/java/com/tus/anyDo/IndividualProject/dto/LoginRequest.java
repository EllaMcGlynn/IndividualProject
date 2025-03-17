package com.tus.anyDo.IndividualProject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginRequest {
    private String username;
    private String password;
}