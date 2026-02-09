package com.myPg.repository;

import com.myPg.entity.TenantAllocation;
import com.myPg.entity.User;
import com.myPg.entity.Room;
import com.myPg.entity.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantAllocationRepository extends JpaRepository<TenantAllocation, UUID> {

    // Get active allocation for a tenant
    Optional<TenantAllocation> findByTenantAndActiveTrue(User tenant);

    // Get all active allocations for a hostel
    List<TenantAllocation> findByHostelAndActiveTrue(Hostel hostel);

    // Get all active allocations for a room
    List<TenantAllocation> findByRoomAndActiveTrue(Room room);

    // Optional: find allocation history for tenant
    List<TenantAllocation> findByTenant(User tenant);
}
