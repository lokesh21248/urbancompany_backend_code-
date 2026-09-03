package com.urbanservices.backend.domain.repository;

import com.urbanservices.backend.domain.entity.ProviderProfile;
import com.urbanservices.backend.domain.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderProfileRepository extends JpaRepository<ProviderProfile, Long> {
    Optional<ProviderProfile> findByUserId(Long userId);
    List<ProviderProfile> findByVerificationStatus(VerificationStatus verificationStatus);
    Long countByIsOnlineTrue();
}
