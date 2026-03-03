package com.audit.profile.respository;

import com.audit.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    Optional<Profile> findProfileById(UUID userId);

    Optional<Profile> findProfileByEmail(String email);

    Optional <Profile> findProfileByPhoneNumber(String phoneNumber);

    Optional<Profile> findProfileByAlternateEmail(String alternateEmail);

    boolean existsByEmail(String email);


}
