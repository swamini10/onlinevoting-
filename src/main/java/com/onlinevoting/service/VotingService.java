package com.onlinevoting.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.onlinevoting.constants.EmailConstants;
import com.onlinevoting.dto.BaseDTO;
import com.onlinevoting.dto.ElectionResponseDto;
import com.onlinevoting.dto.VotingDTO;
import com.onlinevoting.dto.VotingDetail;
import com.onlinevoting.dto.VotingHistoryDTO;
import com.onlinevoting.enums.VotingStatus;
import com.onlinevoting.model.Candidate;
import com.onlinevoting.model.Election;
import com.onlinevoting.model.Party;
import com.onlinevoting.model.UserDetail;
import com.onlinevoting.model.Voting;
import com.onlinevoting.repository.VotingRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VotingService {

    private final VotingRepository votingRepository;
    private final CandidateService candidateService;

    private final UserDetailService userDetailService;

    private final PartyService partyService;

    private final EmailService emailService;

    public VotingService(VotingRepository votingRepository, CandidateService candidateService, PartyService partyService, UserDetailService userDetailService, EmailService emailService) {
        this.votingRepository = votingRepository;
        this.candidateService = candidateService;
        this.partyService = partyService;
        this.userDetailService = userDetailService;
        this.emailService = emailService;
    }

    public void saveVoting(Voting voting) {
        votingRepository.save(voting);
    }

    public List<VotingHistoryDTO> getVotingHistory(String emailId) {
       List<UserDetail> userDetail = userDetailService.findUsersByEmail(emailId);
        if(userDetail == null || userDetail.isEmpty()) {
            throw new RuntimeException("User not found with email: " + emailId);
        }
        log.info("VotingService.getEligibleElections - UserDetail: {}", userDetail);
        String voterId = userDetail.get(0).getId().toString();
        List<Voting> votings = votingRepository.findByVoterId(voterId);
        List<VotingHistoryDTO> votingDTOs = votings.stream()
                .map(voting -> new VotingHistoryDTO(
                    voting.getElection().getElectionName(),
                    voting.getVoter().getId().toString(),
                    voting.getElectionStartDateTime(),
                    voting.getElectionEndDateTime(),
                    voting.getCandidateId() != null ? VotingStatus.YES : VotingStatus.NO
                ))
                .toList();
        return votingDTOs;
    }

    public Long getTotalVotesByElectionId(Long electionId) {
        return votingRepository.countByElection_IdAndCandidateIdIsNotNull(electionId);
    }

    public Long getVotesForCandidateInElection(Long candidateId, Long electionId) {
        List<Voting> votings = votingRepository.findAllByElection(electionId);
        return votings.stream()
                .filter(voting -> voting.getCandidateId() != null && voting.getCandidateId().equals(candidateId.toString()))
                .count();
    }

    public void voteCandidate(VotingDTO votingDTO) {
        Voting voting = votingRepository.findById(votingDTO.getVoterId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid voting ID: " + votingDTO.getVoterId()));
        voting.setCandidateId(votingDTO.getCandidateId().toString());
        voting.setUpdateBy(voting.getId().toString());
        voting.setUpdatedDate(LocalDateTime.now());
        log.info("VotingService.voteCandidate - Voting updated: {}", voting);
        // send email after voting is cast
        votingRepository.save(voting);
        try{
          this.emailService.sendEmailWithTemplate(
            voting.getVoter().getEmailId(),
           EmailConstants.VOTE_CONFIRMATION_SUBJECT,
           EmailConstants.VOTE_CONFIRMATION_TEMPLATE,
            Map.of(
                "voterName", voting.getVoter().getFullName(),
                "electionName", voting.getElection().getElectionName(),
                "votingDate", voting.getElectionStartDateTime().toLocalDate().toString(),
                "voterId", voting.getVoter().getId().toString()
                
            )
        );
        }catch(Exception e){
            log.error("Error sending vote confirmation email: {}", e.getMessage());
        }
    }

    public void createVotingEntries(Long electionId, List<UserDetail> voters, Election election) {
        for (UserDetail voter : voters) {
            Voting voting = new Voting();
            Election election2Election = new Election();
            election2Election.setId(electionId);
            voting.setElection(election2Election);
            voting.setVoter(voter);
            voting.setCandidateId(null); // keep candidateId null initially for uncast votes
            voting.setActive(true);

            // TO GET ELECTION START AND END DATE TIME
            LocalDate electionDate = election.getElectionDate();
            LocalDateTime electionStartDateTime = electionDate.atTime(7, 30); // 7:30 AM
            LocalDateTime electionEndDateTime = electionDate.atTime(17, 30);   // 5:30 PM
            voting.setElectionStartDateTime(electionStartDateTime);
            voting.setElectionEndDateTime(electionEndDateTime);
            votingRepository.save(voting);
        }
    }   

    public List<BaseDTO> getEligibleElections(String emailId) {
        // Fetch voterId using emailId
        List<UserDetail> userDetail = userDetailService.findUsersByEmail(emailId);
        if(userDetail == null || userDetail.isEmpty()) {
            throw new RuntimeException("User not found with email: " + emailId);
        }
        log.info("VotingService.getEligibleElections - UserDetail: {}", userDetail);
        String voterId = userDetail.get(0).getId().toString();
        List<Voting> votings = votingRepository.findByVoterId(voterId);
        List<BaseDTO> eligibleElections = votings.stream()
                .map(voting-> toConvertElection(voting))
                .toList();
       log.info("VotingService.getEligibleElections - Eligible Elections: {}", eligibleElections);
        return eligibleElections;
    }
    
   private BaseDTO toConvertElection(Voting voting) {
        Election election = voting.getElection();
        return new BaseDTO(
            election.getId(),
            election.getElectionName()
        );
    }

    public VotingDetail getVotingDetail(String emailId) {
        // Fetch voterId using emailId
        List<UserDetail> userDetail = userDetailService.findUsersByEmail(emailId);
        if(userDetail == null || userDetail.isEmpty()) {
            throw new RuntimeException("User not found with email: " + emailId);
        }

        String voterId = userDetail.get(0).getId().toString();
        List<Voting> voting = votingRepository.findByVoterIdAndEligibleForVoting(voterId);
        if(voting != null && !voting.isEmpty()) {
          Voting votingEntity = voting.get(0);
            // Map Voting entities to VotingDetail DTO
            VotingDetail votingDetail = new VotingDetail();
            votingDetail.setVotingId(votingEntity.getId());
            // Populate votingDetail fields as needed
            if(votingEntity.getCandidateId() == null ){
                // Vote has not been cast yet
                votingDetail.setElectionId(votingEntity.getElection().getId());
                votingDetail.setElectionName(votingEntity.getElection().getElectionName());
                votingDetail.setElectionStartTime(votingEntity.getElectionStartDateTime());
                votingDetail.setElectionEndTime(votingEntity.getElectionEndDateTime());
                List<Candidate> candidates = candidateService.getCandidateEntityByElectionId(votingEntity.getElection().getId());
                // You might want to set this list to votingDetail if it has a field for candidates
                // votingDetail.setCandidates(candidates);
                if(candidates != null && !candidates.isEmpty()) {
                    List<com.onlinevoting.dto.CandidateInfo> candidateInfos = candidates.stream().map(candidate -> {
                        com.onlinevoting.dto.CandidateInfo info = new com.onlinevoting.dto.CandidateInfo();
                        info.setCandidateId(candidate.getId());
                        info.setCandidateName(candidate.getFullName());
                        
                        Party party = partyService.getPartyById(candidate.getParty().getId());   
                        if(party == null) {
                            throw new RuntimeException("Party not found with id: " + candidate.getParty().getId());
                        }
                        info.setPartyName(party.getName());
                        info.setSymbolUrl(party.getLogoUrl());

                        info.setPhoto(candidate.getCandidatePhoto());
                        return info;
                    }).toList();
                    votingDetail.setCandidates(candidateInfos);
                }

            }else {
                throw new IllegalArgumentException("Vote has already been cast for this voter.");
            }
            return votingDetail;
        }
        throw new IllegalArgumentException("Vote has already been cast for this voter.");  
      }

    public List<Voting> getAllVotingsByElectionId(Long electionId) {
        return votingRepository.findAllByElection(electionId);
    }
}
