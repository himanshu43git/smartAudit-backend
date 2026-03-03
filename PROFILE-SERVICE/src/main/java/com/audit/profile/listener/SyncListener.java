package com.audit.profile.listener;

import com.audit.profile.io.request.ProfileSyncRequest;
import com.audit.profile.service.ProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class SyncListener {

    private final ProfileService profileService;

    public SyncListener(ProfileService service) {
        this.profileService = service;
    }

    @Bean
    public Consumer<ProfileSyncRequest> UserCreatedEvent() {
        // Handle the user created event
        // Add your logic here to process the user creation event
        System.out.println("Received user created event: ");
        return profileService::addSyncProfile;
    }

}
