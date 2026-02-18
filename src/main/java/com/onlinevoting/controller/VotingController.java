package com.onlinevoting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onlinevoting.dto.ApiResponse;
import com.onlinevoting.dto.VotingDTO;
import com.onlinevoting.model.Voting;
import com.onlinevoting.service.TokenService;
import com.onlinevoting.service.VotingService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
public class VotingController {

    private final VotingService votingService;
    private final TokenService tokenService;

    public VotingController(VotingService votingService, TokenService tokenService) {
        this.votingService = votingService;
        this.tokenService = tokenService;
    }

    @GetMapping(path = "/v1/voting/", produces = "application/json")
    public ResponseEntity<ApiResponse> getVotingListForVote(HttpServletRequest request) {
          String emailId = tokenService.extractEmailId(request);
        return ResponseEntity.ok(new ApiResponse(true, votingService.getVotingDetail(emailId), null));
    }

    @PatchMapping(path = "/v1/voting/submit_vote", produces = "application/json",consumes = "application/json")
    public ResponseEntity<ApiResponse> voteCandidate(@RequestBody VotingDTO votingDTO) {
          votingService.voteCandidate(votingDTO);
        return ResponseEntity.ok(new ApiResponse(true, "Vote cast successfully", null));
    }

    @GetMapping(path = "/v1/voting/eligible_election", produces = "application/json")
    public ResponseEntity<ApiResponse> eligibleElection(HttpServletRequest request) {
        String emailId = tokenService.extractEmailId(request);
        return ResponseEntity.ok(new ApiResponse(true, votingService.getEligibleElections(emailId), null));
    }

    @GetMapping(path = "/v1/voting/voting_history", produces = "application/json")
    public ResponseEntity<ApiResponse> getVotingDetail(HttpServletRequest request) {
        String emailId = tokenService.extractEmailId(request);
        return ResponseEntity.ok(new ApiResponse(true, votingService.getVotingHistory(emailId), null));
    }

}
