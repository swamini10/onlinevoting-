package com.onlinevoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "election_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectionResult extends AuditDetail {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Party is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", referencedColumnName = "id")
    private Party party;

    @NotNull(message = "Election is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id", referencedColumnName = "id")
    private Election election;

    @NotNull(message = "Candidate is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", referencedColumnName = "id")
    private Candidate candidate;

    @Column(name = "votes_received", nullable = false)
    private Long votesReceived;

    @Column(name = "total_election_voted", nullable = false)
    private Long totalElectionVoted;
    
}
