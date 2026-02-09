package com.myPg.controller.admin;

import com.myPg.dto.ApiDataResponse;
import com.myPg.dto.ApiResponse;
import com.myPg.dto.admin.CreateHostelRequest;
import com.myPg.entity.Hostel;
import com.myPg.service.admin.HostelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/hostels")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
public class AdminHostelController {

    private final HostelService hostelService;

    @PostMapping
    public ResponseEntity<ApiDataResponse<Hostel>> createHostel(@Valid @RequestBody CreateHostelRequest request) {
        Hostel hostel = hostelService.createHostel(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiDataResponse<>(true, "Hostel created successfully", hostel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Hostel>> getHostel(@PathVariable(name="id") UUID id) {
        Hostel hostel = hostelService.getHostelById(id);
        return ResponseEntity.ok(new ApiDataResponse<>(true, "Hostel fetched successfully", hostel));
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<ApiDataResponse<List<Hostel>>> getHostelsByAdmin(@PathVariable(name="adminId") UUID adminId) {
        List<Hostel> hostels = hostelService.getHostelsByAdmin(adminId);
        return ResponseEntity.ok(new ApiDataResponse<>(true, "Hostels fetched successfully", hostels));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Hostel>> updateHostel(@PathVariable(name="id") UUID id,
                                                                @Valid @RequestBody CreateHostelRequest request) {
        Hostel updated = hostelService.updateHostel(id, request);
        return ResponseEntity.ok(new ApiDataResponse<>(true, "Hostel updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteHostel(@PathVariable(name="id") UUID id) {
        hostelService.deleteHostel(id);
        return ResponseEntity.ok(new ApiResponse(true, "Hostel deleted successfully"));
    }
}
