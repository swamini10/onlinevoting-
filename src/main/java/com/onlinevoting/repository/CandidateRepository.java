package com.onlinevoting.repository;
import com.onlinevoting.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    public List<Candidate> findByStatus(String status);

    public List<Candidate> findByElection_Id(Long electionId);

}