package com.myPg.service.admin;

import com.myPg.dto.admin.AllocateTenantRequest;
import com.myPg.entity.TenantAllocation;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing tenant allocations in hostels.
 */
public interface AllocationService {

    TenantAllocation allocateTenant(AllocateTenantRequest request);

    TenantAllocation getAllocationById(UUID allocationId);

    List<TenantAllocation> getAllocationsByHostel(UUID hostelId);

    TenantAllocation getActiveAllocationByTenant(UUID tenantId);

    void deallocateTenant(UUID allocationId);
}
