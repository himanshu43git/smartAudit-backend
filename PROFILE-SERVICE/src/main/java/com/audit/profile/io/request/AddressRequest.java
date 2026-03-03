package com.audit.profile.io.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddressRequest {

    @Pattern(regexp = "^[A-Za-z0-9\\s]+$", message = "Street name must contain only alphanumeric characters and spaces")
    private String streetName;

    @Pattern(regexp = "^[A-Za-z0-9\\s]+$", message = "Street number must contain only alphanumeric characters and spaces")
    private String streetNumber;

    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "City must contain only alphabetic characters and spaces")
    private String city;

    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "State must contain only alphabetic characters and spaces")
    private String state;

    @Pattern(regexp = "^[A-Za-z0-9\\s-]+$", message = "Postal code must contain only alphanumeric characters, spaces, and hyphens")
    private String postalCode;

    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Country must contain only alphabetic characters and spaces")
    private String country;
}