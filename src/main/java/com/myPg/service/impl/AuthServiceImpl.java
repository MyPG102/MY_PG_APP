package com.myPg.service.impl;

import com.myPg.dto.LoginRequest;
import com.myPg.dto.LoginResponse;
import com.myPg.dto.RegisterRequest;
import com.myPg.entity.User;
import com.myPg.exception.CustomException;
import com.myPg.repository.UserRepository;
import com.myPg.security.JwtUtil;
import com.myPg.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final int MAX_LOGIN_ATTEMPTS = 5;

    // ================= REGISTER =================
    @Override
    public void register(RegisterRequest request) {

        String email = request.getEmail().trim().toLowerCase();
        String mobile = request.getMobile().trim();

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new CustomException("PASSWORD_MISMATCH", "Passwords do not match");
        }

        if (userRepository.existsByEmailAndIsDeletedFalse(email)) {
            throw new CustomException("EMAIL_ALREADY_EXISTS", "This email is already registered");
        }

        if (userRepository.existsByMobileAndIsDeletedFalse(mobile)) {
            throw new CustomException("MOBILE_ALREADY_EXISTS", "This mobile number is already registered");
        }

        User user = User.builder()
                .name(request.getName().trim())
                .email(email)
                .mobile(mobile)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .userType(request.getUserType())
                .build();

        userRepository.save(user);
    }

    // ================= LOGIN =================
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = request.getUsername().contains("@")
                ? userRepository.findByEmailAndIsDeletedFalse(request.getUsername().toLowerCase())
                    .orElseThrow(() -> new CustomException("USER_NOT_FOUND", "User does not exist"))
                : userRepository.findByMobileAndIsDeletedFalse(request.getUsername())
                    .orElseThrow(() -> new CustomException("USER_NOT_FOUND", "User does not exist"));

        if (!user.getIsActive()) {
            throw new CustomException("ACCOUNT_INACTIVE", "Account is inactive");
        }

        if (user.getIsDeleted()) {
            throw new CustomException("ACCOUNT_DELETED", "Account not available");
        }

        if (user.getLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
            throw new CustomException("ACCOUNT_LOCKED", "Account temporarily locked");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            userRepository.save(user);
            throw new CustomException("INVALID_PASSWORD", "Invalid credentials");
        }

        // SUCCESS LOGIN
        user.setLoginAttempts(0);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(
                user.getUserId().toString(),
                user.getUserType().name()
        );

        return new LoginResponse(token, user.getUserType());
    }

    // ================= GET USER =================
    @Override
    public User getUserById(UUID id) {
        return userRepository.findByUserIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", "User does not exist"));
    }

    // ================= GET ALL USERS =================
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getIsDeleted())
                .toList();
    }

    // ================= UPDATE USER =================
    @Override
    public void updateUser(UUID id, RegisterRequest request) {

        User user = getUserById(id);

        String email = request.getEmail().trim().toLowerCase();
        String mobile = request.getMobile().trim();

        if (!user.getEmail().equalsIgnoreCase(email)
                && userRepository.existsByEmailAndIsDeletedFalse(email)) {
            throw new CustomException("EMAIL_ALREADY_EXISTS", "Email already registered");
        }

        if (!user.getMobile().equals(mobile)
                && userRepository.existsByMobileAndIsDeletedFalse(mobile)) {
            throw new CustomException("MOBILE_ALREADY_EXISTS", "Mobile already registered");
        }

        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setMobile(mobile);
        user.setUserType(request.getUserType());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                throw new CustomException("PASSWORD_MISMATCH", "Passwords do not match");
            }
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
    }

    // ================= DELETE USER (SOFT DELETE) =================
    @Override
    public void deleteUser(UUID id) {
        User user = getUserById(id);
        user.setIsDeleted(true);
        userRepository.save(user);
    }
}
