package com.audit.profile.service;

import com.audit.profile.io.request.ProfileRequest;
import com.audit.profile.io.request.ProfileSyncRequest;
import com.audit.profile.io.response.ProfileResponse;
import com.audit.profile.io.response.ProfileSyncResponse;

import java.util.UUID;

public interface ProfileService {

    ProfileSyncResponse addSyncProfile(ProfileSyncRequest profileRequest);

    ProfileResponse createProfile(ProfileRequest profileRequest);

    ProfileResponse getProfileById(String profileId);

    ProfileResponse updateProfile(ProfileRequest profileRequest, UUID id);

    String updateProfilePicture(String profileId, String imageUrl);

    boolean deactivateProfileById(String profileId);

    boolean activateProfileById(String profileId);

    void syncVerifyToProfileService(String email);

}
