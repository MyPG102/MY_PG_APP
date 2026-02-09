package com.myPg.controller.admin;

import com.myPg.dto.ApiDataResponse;
import com.myPg.dto.ApiResponse;
import com.myPg.dto.admin.AllocateTenantRequest;
import com.myPg.entity.TenantAllocation;
import com.myPg.service.admin.AllocationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/allocations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminAllocationController {

    private final AllocationService allocationService;

    // ================= CREATE ALLOCATION =================
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ApiDataResponse<TenantAllocation> allocateTenant(@Valid @RequestBody AllocateTenantRequest request) {
        return new ApiDataResponse<>(true, "Tenant allocated", allocationService.allocateTenant(request));
    }

    // ================= GET ALLOCATIONS BY HOSTEL =================
    @GetMapping("/hostel/{hostelId}")
    @SecurityRequirement(name = "bearerAuth")
    public ApiDataResponse<List<TenantAllocation>> getAllocationsByHostel(@PathVariable(name="hostelId") UUID hostelId) {
        return new ApiDataResponse<>(true, "Allocations fetched", allocationService.getAllocationsByHostel(hostelId));
    }

    // ================= GET ACTIVE ALLOCATION FOR TENANT =================
    @GetMapping("/tenant/{tenantId}")
    @SecurityRequirement(name = "bearerAuth")
    public ApiDataResponse<TenantAllocation> getActiveAllocation(@PathVariable(name="tenantId") UUID tenantId) {
        return new ApiDataResponse<>(true, "Active allocation fetched", allocationService.getActiveAllocationByTenant(tenantId));
    }

    // ================= DEACTIVATE / DELETE ALLOCATION =================
    @DeleteMapping("/{allocationId}")
    @SecurityRequirement(name = "bearerAuth")
    public ApiResponse deactivateAllocation(@PathVariable(name="allocationId") UUID allocationId) {
        allocationService.deallocateTenant(allocationId);
        return new ApiResponse(true, "Allocation deactivated successfully");
    }
}
