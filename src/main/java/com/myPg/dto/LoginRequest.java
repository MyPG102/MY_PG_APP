package com.myPg.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email or mobile is required")
    private String username; // email OR mobile

    @NotBlank(message = "Password is required")
    private String password;
}
