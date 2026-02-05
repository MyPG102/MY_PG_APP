package com.myPg.service;

import com.myPg.dto.LoginRequest;
import com.myPg.dto.LoginResponse;
import com.myPg.dto.RegisterRequest;
import com.myPg.entity.User;

import java.util.List;
import java.util.UUID;

public interface AuthService {

    // ================= AUTH =================
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);

    // ================= USER MANAGEMENT =================
    User getUserById(UUID id);
    List<User> getAllUsers();
    void updateUser(UUID id, RegisterRequest request);
    void deleteUser(UUID id);
}
