package com.audit.profile.model;

import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String streetName;

    private String streetNumber;

    private String city;

    private String state;

    private String postalCode;

    private String country;
}
