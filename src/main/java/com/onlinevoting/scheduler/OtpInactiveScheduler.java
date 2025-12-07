package com.onlinevoting.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.onlinevoting.model.UserOtpDetails;
import com.onlinevoting.repository.UserOtpDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OtpInactiveScheduler {

    @Autowired
    private UserOtpDetailsRepository userOtpDetailsRepository;

    @Scheduled(initialDelay = 5000,  fixedDelay = 30000) // Runs every 60 seconds
    public void deactivateInactiveOtps() {
        log.info("Running OTP Inactive Scheduler Task");
        // Implementation for deactivating inactive OTPs
        Long threshold = System.currentTimeMillis() - 5 * 60 * 1000; // 5 minutes ago in milliseconds
        List<UserOtpDetails> inactiveOtps = userOtpDetailsRepository.findByIsOtpUsedFalseAndIsActiveTrueAndExpiryTimeBefore(threshold);
        for (var otpDetails : inactiveOtps) {   
             log.debug("Deactivating OTP with ID: {}", otpDetails.getId());
            otpDetails.setActive(Boolean.FALSE);
            userOtpDetailsRepository.save(otpDetails);
        }
         log.info("Completed OTP Inactive Scheduler Task");
    }
}
