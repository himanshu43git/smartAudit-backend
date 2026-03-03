package com.audit.profile.controller;

import com.audit.profile.io.request.ProfileRequest;
import com.audit.profile.io.request.ProfileSyncRequest;
import com.audit.profile.io.response.ProfileResponse;
import com.audit.profile.io.response.ProfileSyncResponse;
import com.audit.profile.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @PostMapping("/sync")
    public ResponseEntity<ProfileSyncResponse> syncProfile(@RequestBody @Valid ProfileSyncRequest profileRequest){

        ProfileSyncResponse response = profileService.addSyncProfile(profileRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }


    @PostMapping("/create")
    public ResponseEntity<ProfileResponse> createProfile(@RequestBody @Valid ProfileRequest profileRequest){

        ProfileResponse response = profileService.createProfile(profileRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(@RequestBody @Valid ProfileRequest profileRequest, @PathVariable("id") String id){

        ProfileResponse response = profileService.updateProfile(profileRequest, UUID.fromString(id));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProfileResponse> getProfileById(@PathVariable("id") String id){

        ProfileResponse response = profileService.getProfileById(id);

        return ResponseEntity
                .status((HttpStatus.OK))
                .body(response);

    }

//    TODO: Implement image upload handling later when s3 is integrated
    @PutMapping("/updateProfilePicture/{id}/{pictureUrl}")
    public ResponseEntity<String> updateProfilePicture(@PathVariable("id") String id, @PathVariable("pictureUrl") String pictureUrl){

        // Assuming the service has a method to update profile picture
        profileService.updateProfilePicture(id, pictureUrl);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Profile picture updated successfully");

    }

//    TODO: Implement image upload handling later when s3 is integrated
    @PutMapping("/deleteProfilePicture/{id}")
    public ResponseEntity<String> deleteProfilePicture(@PathVariable("id") String id){

        // Assuming the service has a method to delete profile picture
        profileService.updateProfilePicture(id, null);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Profile picture deleted successfully");

    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Boolean> deactivateProfile(@PathVariable("id") String id){

        profileService.deactivateProfileById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(true);

    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<Boolean> activateProfile(@PathVariable("id") String id){

        profileService.activateProfileById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(true);

    }

}
