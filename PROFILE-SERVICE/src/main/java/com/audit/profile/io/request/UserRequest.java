package com.audit.profile.io.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 72, message = "password must be between 6 and 72 characters")
    private String password;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "phoneNumber must be a valid phone number")
    private String phoneNumber;

}
