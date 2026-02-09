package com.myPg.service.impl.admin;

import com.myPg.dto.admin.AllocateTenantRequest;
import com.myPg.entity.Hostel;
import com.myPg.entity.Room;
import com.myPg.entity.TenantAllocation;
import com.myPg.entity.User;
import com.myPg.exception.CustomException;
import com.myPg.repository.HostelRepository;
import com.myPg.repository.RoomRepository;
import com.myPg.repository.TenantAllocationRepository;
import com.myPg.repository.UserRepository;
import com.myPg.service.admin.AllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AllocationServiceImpl implements AllocationService {

    private final TenantAllocationRepository allocationRepository;
    private final UserRepository userRepository;
    private final HostelRepository hostelRepository;
    private final RoomRepository roomRepository;

    @Override
    public TenantAllocation allocateTenant(AllocateTenantRequest request) {

        // ✅ Fetch tenant, hostel, room by UUID
        User tenant = userRepository.findByUserIdAndIsDeletedFalse(request.getTenantId())
                .orElseThrow(() -> new CustomException("TENANT_NOT_FOUND", "Tenant not found"));

        Hostel hostel = hostelRepository.findById(request.getHostelId())
                .orElseThrow(() -> new CustomException("HOSTEL_NOT_FOUND", "Hostel not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new CustomException("ROOM_NOT_FOUND", "Room not found"));

        // ✅ Check room capacity
        if (room.getOccupied() >= room.getCapacity()) {
            throw new CustomException("ROOM_FULL", "Room capacity reached");
        }

        // ✅ Check if tenant already has active allocation
        allocationRepository.findByTenantAndActiveTrue(tenant)
                .ifPresent(a -> {
                    throw new CustomException("TENANT_ALREADY_ALLOCATED", "Tenant already allocated to a room");
                });

        // ✅ Create allocation
        TenantAllocation allocation = TenantAllocation.builder()
                .tenant(tenant)
                .hostel(hostel)
                .room(room)
                .active(true)
                .build();

        // ✅ Increment room occupancy
        room.setOccupied(room.getOccupied() + 1);
        roomRepository.save(room);

        return allocationRepository.save(allocation);
    }

    @Override
    public TenantAllocation getAllocationById(UUID allocationId) {
        return allocationRepository.findById(allocationId)
                .orElseThrow(() -> new CustomException("ALLOCATION_NOT_FOUND", "Allocation not found"));
    }

    @Override
    public List<TenantAllocation> getAllocationsByHostel(UUID hostelId) {
        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new CustomException("HOSTEL_NOT_FOUND", "Hostel not found"));
        return allocationRepository.findByHostelAndActiveTrue(hostel);
    }

    @Override
    public TenantAllocation getActiveAllocationByTenant(UUID tenantId) {
        User tenant = userRepository.findByUserIdAndIsDeletedFalse(tenantId)
                .orElseThrow(() -> new CustomException("TENANT_NOT_FOUND", "Tenant not found"));
        return allocationRepository.findByTenantAndActiveTrue(tenant)
                .orElseThrow(() -> new CustomException("ALLOCATION_NOT_FOUND", "No active allocation for tenant"));
    }

    @Override
    public void deallocateTenant(UUID allocationId) {
        TenantAllocation allocation = getAllocationById(allocationId);

        if (!allocation.isActive()) {
            throw new CustomException("ALLOCATION_ALREADY_INACTIVE", "Allocation already inactive");
        }

        // ✅ Mark allocation inactive
        allocation.setActive(false);
        allocationRepository.save(allocation);

        // ✅ Reduce room occupancy
        Room room = allocation.getRoom();
        room.setOccupied(room.getOccupied() - 1);
        roomRepository.save(room);
    }
}
