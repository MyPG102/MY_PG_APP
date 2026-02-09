package com.myPg.dto.admin;

import com.myPg.enumm.HostelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateHostelRequest {

    @NotBlank(message = "Hostel name is required")
    @Size(min = 2, max = 100, message = "Hostel name must be between 2 and 100 characters")
    private String name;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @NotNull(message = "Hostel type is required")
    private HostelType type;

    @NotNull(message = "Admin ID is required")
    private String adminId; // UUID as String, will be converted in service
}
