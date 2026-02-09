package com.myPg.service.impl.admin;

import com.myPg.dto.admin.CreateHostelRequest;
import com.myPg.entity.Hostel;
import com.myPg.entity.User;
import com.myPg.exception.CustomException;
import com.myPg.repository.HostelRepository;
import com.myPg.repository.UserRepository;
import com.myPg.service.admin.HostelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HostelServiceImpl implements HostelService {

    private final HostelRepository hostelRepository;
    private final UserRepository userRepository;

    @Override
    public Hostel createHostel(CreateHostelRequest request) {

        UUID adminId;
        try {
            adminId = UUID.fromString(request.getAdminId());
        } catch (IllegalArgumentException ex) {
            throw new CustomException("INVALID_ADMIN_ID", "Admin ID is invalid");
        }

        User admin = userRepository.findByUserIdAndIsDeletedFalse(adminId)
                .orElseThrow(() -> new CustomException("ADMIN_NOT_FOUND", "Admin not found"));

        Hostel hostel = Hostel.builder()
                .name(request.getName().trim())
                .address(request.getAddress() != null ? request.getAddress().trim() : null)
                .type(request.getType())
                .admin(admin)
                .build();

        return hostelRepository.save(hostel);
    }

    @Override
    public Hostel updateHostel(UUID hostelId, CreateHostelRequest request) {
        Hostel hostel = getHostelById(hostelId);

        if (request.getName() != null && !request.getName().isBlank()) {
            hostel.setName(request.getName().trim());
        }

        if (request.getAddress() != null) {
            hostel.setAddress(request.getAddress().trim());
        }

        if (request.getType() != null) {
            hostel.setType(request.getType());
        }

        return hostelRepository.save(hostel);
    }

    @Override
    public void deleteHostel(UUID hostelId) {
        Hostel hostel = getHostelById(hostelId);
        hostelRepository.delete(hostel); // Or implement soft delete if needed
    }

    @Override
    public Hostel getHostelById(UUID hostelId) {
        return hostelRepository.findById(hostelId)
                .orElseThrow(() -> new CustomException("HOSTEL_NOT_FOUND", "Hostel not found"));
    }

    @Override
    public List<Hostel> getHostelsByAdmin(UUID adminId) {
        User admin = userRepository.findByUserIdAndIsDeletedFalse(adminId)
                .orElseThrow(() -> new CustomException("ADMIN_NOT_FOUND", "Admin not found"));
        return hostelRepository.findByAdmin(admin);
    }
}
