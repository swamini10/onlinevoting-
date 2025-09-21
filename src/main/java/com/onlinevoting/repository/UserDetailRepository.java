package com.onlinevoting.repository;

import com.onlinevoting.model.UserDetail;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    UserDetail findByEmailId(String emailId);
    Optional<UserDetail> findByIdAndIsActiveTrue(Long id);
}
