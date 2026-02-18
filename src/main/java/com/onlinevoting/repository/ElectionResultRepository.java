package com.onlinevoting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionResultRepository extends JpaRepository<com.onlinevoting.model.ElectionResult, Long> {
    List<com.onlinevoting.model.ElectionResult> findByElectionId(Long electionId);
}
