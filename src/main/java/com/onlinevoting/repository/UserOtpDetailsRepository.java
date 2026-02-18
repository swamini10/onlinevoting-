package com.onlinevoting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlinevoting.model.UserOtpDetails;

public interface UserOtpDetailsRepository extends JpaRepository<UserOtpDetails, Long> {
      Optional<UserOtpDetails> findByUserDetailIdAndIsOtpUsedFalseAndIsActiveTrue(Long userId);
      
      // Find all OTP records that are not used, active, and whose expiryTime (epoch millis) is before the provided threshold.
      // Call with: Long thresholdMillis = Instant.now().minus(Duration.ofMinutes(5)).toEpochMilli();
      List<UserOtpDetails> findByIsOtpUsedFalseAndIsActiveTrueAndExpiryTimeBefore(Long thresholdMillis);
      
}
