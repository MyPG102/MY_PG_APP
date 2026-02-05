package com.myPg.dto;

import com.myPg.enumm.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private UserType userType;
}
