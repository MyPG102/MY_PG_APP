package com.myPg.dto.admin;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllocateTenantRequest {

    @NotNull(message = "Tenant ID is required")
    private UUID tenantId; // UUID as String

    @NotNull(message = "Hostel ID is required")
    private UUID hostelId; // UUID as String

    @NotNull(message = "Room ID is required")
    private UUID roomId; // UUID as String
}
