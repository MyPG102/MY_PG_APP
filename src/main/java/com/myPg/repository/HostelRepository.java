package com.myPg.repository;

import com.myPg.entity.Hostel;
import com.myPg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface HostelRepository extends JpaRepository<Hostel, UUID> {

    // Get all hostels owned by an admin
    List<Hostel> findByAdmin(User admin);
}
