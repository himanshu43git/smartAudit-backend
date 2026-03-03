package com.audit.profile.service.impl;

import com.audit.profile.exceptions.UserAlreadyExistsException;
import com.audit.profile.exceptions.UserNotFoundException;
import com.audit.profile.io.request.AddressRequest;
import com.audit.profile.io.request.ProfileRequest;
import com.audit.profile.io.request.ProfileSyncRequest;
import com.audit.profile.io.response.ProfileResponse;
import com.audit.profile.io.response.ProfileSyncResponse;
import com.audit.profile.model.Address;
import com.audit.profile.model.Profile;
import com.audit.profile.respository.ProfileRepository;
import com.audit.profile.service.ProfileService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public ProfileSyncResponse addSyncProfile(ProfileSyncRequest profileRequest) {

        if(profileRepository.existsByEmail(profileRequest.getEmail())){
            throw new UserAlreadyExistsException("User with email " + profileRequest.getEmail() + " already exists.");
        }

        Profile profile = Profile.builder()
                .email(profileRequest.getEmail())
                .password(profileRequest.getPassword())
                .phoneNumber(profileRequest.getPhoneNumber())
                .build();


        profile = profileRepository.save(profile);

        return ProfileSyncResponse.builder()
                .profileSyncId(profile.getId())
                .build();
    }

    @Override
    public ProfileResponse createProfile(ProfileRequest profileRequest) {
        return null;
    }

    @Override
    public ProfileResponse getProfileById(String profileId) {

        if(profileId == null || profileId.isEmpty()){
            return null;
        }

        Optional<Profile> profile = profileRepository.findProfileById(UUID.fromString(profileId));

        if(profile.isEmpty()){
            return null;
        }

        return ProfileResponse.builder()
                .id(profile.get().getId())
                .email(profile.get().getEmail())
                .alternateEmail(profile.get().getAlternateEmail())
                .firstName(profile.get().getFirstName())
                .lastName(profile.get().getLastName())
                .phoneNumber(profile.get().getPhoneNumber())
                .profilePictureUrl(profile.get().getProfilePictureUrl())
                .address(profile.get().getAddress())
                .isActive(profile.get().isActive())
                .age(LocalDate.now().getYear() - profile.get().getDateOfBirth().getYear())
                .isVerified(true)
                .createdAt(profile.get().getCreatedAt())
                .updatedAt(profile.get().getUpdatedAt())
                .build();

    }

    @Override
    public ProfileResponse updateProfile(ProfileRequest request, UUID id) {

        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Profile not found with id: " + id));

        // Email uniqueness check (ignore same profile)
        if (request.getEmail() != null &&
                !request.getEmail().equals(profile.getEmail()) &&
                profileRepository.existsByEmail(request.getEmail())) {

            throw new UserAlreadyExistsException(
                    "User with email " + request.getEmail() + " already exists."
            );
        }

        // Update fields
        profile.setEmail(request.getEmail());
        profile.setAlternateEmail(request.getAlternateEmail());
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setProfilePictureUrl(request.getProfilePictureUrl());

        if (request.getDateOfBirth() != null) {
            profile.setDateOfBirth(request.getDateOfBirth());
        }

        // Update Address (Embedded)
        if (request.getAddress() != null) {
            AddressRequest ar = request.getAddress();

            Address address = profile.getAddress() != null
                    ? profile.getAddress()
                    : new Address();

            address.setStreetName(ar.getStreetName());
            address.setStreetNumber(ar.getStreetNumber());
            address.setCity(ar.getCity());
            address.setState(ar.getState());
            address.setPostalCode(ar.getPostalCode());
            address.setCountry(ar.getCountry());

            profile.setAddress(address);
        }

        Profile updatedProfile = profileRepository.save(profile);

        return ProfileResponse.builder()
                .id(updatedProfile.getId())
                .email(updatedProfile.getEmail())
                .alternateEmail(updatedProfile.getAlternateEmail())
                .firstName(updatedProfile.getFirstName())
                .lastName(updatedProfile.getLastName())
                .age(
                        updatedProfile.getDateOfBirth() != null
                                ? java.time.Period.between(
                                updatedProfile.getDateOfBirth(),
                                java.time.LocalDate.now()
                        ).getYears()
                                : 0
                )
                .phoneNumber(updatedProfile.getPhoneNumber())
                .profilePictureUrl(updatedProfile.getProfilePictureUrl())
                .address(updatedProfile.getAddress())
                .isActive(updatedProfile.isActive())
                .isVerified(updatedProfile.isVerified())
                .createdAt(updatedProfile.getCreatedAt())
                .updatedAt(updatedProfile.getUpdatedAt())
                .build();
    }


//    todo: implement this after s3 integration
    @Override
    public String updateProfilePicture(String profileId, String imageUrl) {
        return "";
    }

    @Override
    public boolean deactivateProfileById(String profileId) {

        UUID id;
        try {
            id = UUID.fromString(profileId);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid profile ID format: " + profileId);
        }

        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Profile not found with id: " + profileId));

        // If already inactive, return false (idempotent behavior)
        if (!profile.isActive()) {
            return false;
        }

        profile.setActive(false);
        profileRepository.save(profile);

        return true;
    }

    @Override
    public boolean activateProfileById(String profileId) {

        UUID id;
        try {
            id = UUID.fromString(profileId);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid profile ID format: " + profileId);
        }

        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Profile not found with id: " + profileId));

        // If already active, return false (idempotent behavior)
        if (profile.isActive()) {
            return false;
        }

        profile.setActive(true);
        profileRepository.save(profile);

        return true;
    }

    @Override
    public void syncVerifyToProfileService(String email) {

        Optional<Profile> profile = profileRepository.findProfileByEmail(email);
        if(profile.isPresent()){
            profile.get().setVerified(true);
            profileRepository.save(profile.get());
        }
        else {
            throw new UserNotFoundException("Profile not found with email: " + email);
        }

    }

}
