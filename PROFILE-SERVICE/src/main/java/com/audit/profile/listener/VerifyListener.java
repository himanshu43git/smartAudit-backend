package com.audit.profile.listener;

import com.audit.profile.service.ProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class VerifyListener {

    private final ProfileService profileService;

    public VerifyListener(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Bean
    public Consumer<String> UserVerifiedEvent() {
        return profileService::syncVerifyToProfileService;
    }

}
