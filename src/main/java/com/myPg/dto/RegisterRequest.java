package com.myPg.dto;

import com.myPg.enumm.UserType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

	@NotBlank(message = "Please enter your full name")
	@Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
	@Pattern(
	    regexp = "^[A-Za-z]+( [A-Za-z]+)*$",
	    message = "Name should contain only letters and spaces"
	)
	private String name;


	  @NotBlank(message = "Please enter your email address")
	  @Email(message = "Please enter a valid email address")
	  @Size(max = 100, message = "Email address is too long")
	  private String email;


	    @NotBlank(message = "Please enter your mobile number")
	    @Pattern(
	        regexp = "^[6-9][0-9]{9}$",
	        message = "Please enter a valid 10-digit mobile number"
	    )
	    private String mobile;


	    @NotBlank(message = "Please create a password")
	    @Size(min = 8, max = 64, message = "Password must be at least 8 characters long")
	    @Pattern(
	        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
	        message = "Password must include at least one uppercase letter, one lowercase letter, one number, and one special character"
	    )
	    private String password;


	    @NotBlank(message = "Please confirm your password")
	    private String confirmPassword;

	    @NotNull(message = "Please select account type")
	    private UserType userType;
}
