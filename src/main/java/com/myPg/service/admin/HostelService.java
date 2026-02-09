package com.myPg.service.admin;

import com.myPg.dto.admin.CreateHostelRequest;
import com.myPg.entity.Hostel;

import java.util.List;
import java.util.UUID;

public interface HostelService {

    // Create a new hostel
    Hostel createHostel(CreateHostelRequest request);

    // Update existing hostel
    Hostel updateHostel(UUID hostelId, CreateHostelRequest request);

    // Delete hostel (soft delete)
    void deleteHostel(UUID hostelId);

    // Get a single hostel by ID
    Hostel getHostelById(UUID hostelId);

    // Get all hostels for an admin
    List<Hostel> getHostelsByAdmin(UUID adminId);
}
