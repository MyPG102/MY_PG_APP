package com.myPg.repository;

import com.myPg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    // Registration validations
    boolean existsByEmailAndIsDeletedFalse(String email);
    boolean existsByMobileAndIsDeletedFalse(String mobile);

    // Login
    Optional<User> findByEmailAndIsDeletedFalse(String email);
    Optional<User> findByMobileAndIsDeletedFalse(String mobile);

    // GET / UPDATE / DELETE (IMPORTANT)
    Optional<User> findByUserIdAndIsDeletedFalse(UUID userId);
}
