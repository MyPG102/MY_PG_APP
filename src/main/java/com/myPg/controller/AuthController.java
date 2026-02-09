package com.myPg.controller;

import com.myPg.dto.*;
import com.myPg.entity.User;
import com.myPg.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ================= REGISTER (PUBLIC) =================
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Account created successfully"));
    }

    // ================= LOGIN (PUBLIC) =================
    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                new ApiDataResponse<>(true, "Login successful", authService.login(request))
        );
    }

    // ================= GET USER BY ID (PROTECTED) =================
    @GetMapping("/user/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiDataResponse<User>> getUserById(@PathVariable(name="id") UUID id) {
        return ResponseEntity.ok(
                new ApiDataResponse<>(true, "User fetched successfully", authService.getUserById(id))
        );
    }

    // ================= GET ALL USERS (PROTECTED) =================
    @GetMapping("/users")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiDataResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(
                new ApiDataResponse<>(true, "Users fetched successfully", authService.getAllUsers())
        );
    }

    // ================= UPDATE USER (PROTECTED) =================
    @PutMapping("/user/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable(name="id") UUID id,
                                                  @Valid @RequestBody RegisterRequest request) {
        authService.updateUser(id, request);
        return ResponseEntity.ok(new ApiResponse(true, "User updated successfully"));
    }

    // ================= DELETE USER (PROTECTED) =================
    @DeleteMapping("/user/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable(name="id") UUID id) {
        authService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully"));
    }
}
