package com.audit.profile.io.response;

import com.audit.profile.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {

    private UUID id;

    private String email;

    private String alternateEmail;

    private String firstName;

    private String lastName;

    private int age;

    private String phoneNumber;

    private String profilePictureUrl;

    private Address address;

    private boolean isActive = true;

    private boolean isVerified;

    private Instant createdAt;

    private Instant updatedAt;

}
