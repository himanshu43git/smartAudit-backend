package com.audit.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String phoneNumber;

    /**
     * Do NOT store plain OTP in DB. Store a hashed OTP instead (e.g. bcrypt/HMAC).
     * hashedOtp should be produced by hashing the generated numeric/string OTP
     * before saving to DB.
     */
    @Column(length = 128)
    private String hashedOtp;

    /**
     * When the OTP was generated (useful for logging / analytics).
     */
    private Instant otpGeneratedAt;

    /**
     * Absolute expiry time for the current OTP (e.g. generatedAt + 5 minutes).
     */
    private Instant otpExpiry;

    /**
     * Number of verification attempts used for this OTP (increment on failed verify).
     */
    private Integer otpAttempts;

    /**
     * Whether this OTP has already been used to verify the user (one-time).
     */
    private Boolean otpUsed;

    /**
     * Channel the OTP was sent on (SMS, EMAIL, etc).
     */
    @Enumerated(EnumType.STRING)
    private OtpChannel otpChannel;

    /**
     * When the OTP was successfully verified (null if not verified yet).
     */
    private Instant otpVerifiedAt;

    private boolean isVerified = false;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.otpAttempts == null) this.otpAttempts = 0;
        if (this.otpUsed == null) this.otpUsed = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
