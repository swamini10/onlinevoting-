package com.onlinevoting.service;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.onlinevoting.dto.BaseDTO;
import com.onlinevoting.dto.CandidateResponseDTO;
import com.onlinevoting.dto.CandidateVotingDetail;
import com.onlinevoting.dto.ElectionAddressDTO;
import com.onlinevoting.dto.ElectionDataPoint;
import com.onlinevoting.dto.ElectionResponseDto;
import com.onlinevoting.dto.ElectionResultDTO;
import com.onlinevoting.dto.ElectionResultMainDTO;
import com.onlinevoting.dto.StatusUpdateRequestDTO;
import com.onlinevoting.enums.Status;
import com.onlinevoting.model.Candidate;
import com.onlinevoting.model.Election;
import com.onlinevoting.model.ElectionResult;
import com.onlinevoting.model.UserDetail;
import com.onlinevoting.repository.ElectionRepository;
import com.onlinevoting.repository.ElectionResultRepository;
import com.onlinevoting.repository.UserDetailRepository;

import lombok.extern.log4j.Log4j2;

import com.onlinevoting.constants.EmailConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ElectionService {

    private final ElectionRepository electionRepository;
    private final ObjectMapper objectMapper;
    private final CountryService countryService;
    private final StateService stateService;
    private final CityService cityService;
    private final UserDetailService userDetailService;
    private final EmailService emailService;
    private final UserDetailRepository userDetailRepository;
    private final CandidateService candidateService;
    private final VotingService votingService;
    private final ElectionResultRepository electionResultRepository;

    public ElectionService(ElectionRepository electionRepository, CountryService countryService, 
        StateService stateService, CityService cityService, UserDetailService userDetailService, 
        EmailService emailService, UserDetailRepository userDetailRepository,
        CandidateService candidateService, VotingService votingService, ElectionResultRepository electionResultRepository) {
        this.electionRepository = electionRepository;
        this.countryService = countryService;
        this.stateService = stateService;
        this.cityService = cityService;
        this.userDetailService = userDetailService;
        this.emailService = emailService;
        this.userDetailRepository = userDetailRepository;
        this.candidateService = candidateService;
        this.votingService = votingService;
        this.electionResultRepository = electionResultRepository;
        this.objectMapper = new ObjectMapper();
        // Configure ObjectMapper to handle LocalDate properly
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    public void sendElectionNotification(Long electionId) {
        Election election = electionRepository.findById(electionId)
            .orElseThrow(() -> new IllegalArgumentException("Election not found with id: " + electionId));
        
        sendElectionPublishedNotification(election);
    }

    public void publishElection(Long electionId, StatusUpdateRequestDTO statusUpdateRequest) {
        Election election = electionRepository.findById(electionId)
            .orElseThrow(() -> new IllegalArgumentException("Election not found with id: " + electionId));

        election.setNote(statusUpdateRequest.getNote());
        election.setIsPublish(statusUpdateRequest.getIsPublish());

        // Candidate selection logic can be added here
        List<CandidateResponseDTO> selectedCandidates = candidateService.getCandidateByElectionId(electionId);
        // Send email to candidate for selection notification - NEXT STEP
        List<CandidateVotingDetail> candidateVotingDetails = selectedCandidates.stream().map(c -> {
            CandidateVotingDetail detail = new CandidateVotingDetail();
            detail.setCandidateName(c.getCandidateName());
            detail.setParty(c.getParty());
            detail.setLogoUrl(c.getLogo());
            return detail;
        }).collect(Collectors.toList());

        // get all active voters in the election's city
        List<UserDetail> activeVoters = userDetailRepository.findActiveVoters(election.getCity().getId());
        // add entry in voting table
        votingService.createVotingEntries(electionId, activeVoters , election);
        // get all active voters in the election id

        // Send email notification logic can be added here
   
       List<String> voterEmails = activeVoters.stream()
            .map(UserDetail::getEmailId).toList();

            if (statusUpdateRequest.getIsPublish() != null && statusUpdateRequest.getIsPublish()) {
            // Prepare email content
            Map<String, Object> emailModel = new HashMap<>();
            emailModel.put("electionName", election.getElectionName());
            emailModel.put("electionDate", election.getElectionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            emailModel.put("resultDate", election.getResultDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            emailModel.put("note", statusUpdateRequest.getNote());
            emailModel.put("candidates", candidateVotingDetails);    
            // Send email to all active voters in the election's city
            try{
                emailService.sendEmailWithTemplate(voterEmails, EmailConstants.ELECTION_PUBLISHED_SUBJECT, 
               EmailConstants.ELECTION_PUBLISHED_TEMPLATE, emailModel);
            }catch(Exception e){
                System.out.println("Error sending election published emails: " + e.getMessage());
            }

        }
       electionRepository.save(election);
    }
    
    public Election saveElection(String election) {
        try {
            // Convert JSON string to Election object
            Election electionObject = objectMapper.readValue(election, Election.class);
            electionObject.setActive(true);
            electionObject.setStatus(Status.PENDING.getDisplayName());
            electionObject.setIsPublish(false);
            electionObject.setIsResultPublish(false);
            electionObject.setNote("");
            if(electionObject.getElectionDate().isAfter(electionObject.getResultDate())) {
                throw new IllegalArgumentException("Election date must be before result date.");
            }
            if(electionObject.getFormEndDate().isAfter(electionObject.getElectionDate())) {
                throw new IllegalArgumentException("Form end date must be before election date.");
            }

            // Save the election object to database
            return electionRepository.save(electionObject);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse election JSON: " + e.getMessage(), e);
        }
    } 

    public ElectionAddressDTO getElectionById(Long electionId) {
        Election election = electionRepository.findById(electionId)
            .orElseThrow(() -> new IllegalArgumentException("Election not found with id: " + electionId));
        return toDtoWithIdsDto(election);
    }

        public ElectionResponseDto getElectionDetails(Long electionId) {
        Election election = electionRepository.findById(electionId)
            .orElseThrow(() -> new IllegalArgumentException("Election not found with id: " + electionId));
        return toDto(election);
    }



    public List<ElectionResponseDto> getAllElections() {
        return electionRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }


    public void approveElection(Long electionId, String status) {
        Election election = electionRepository.findById(electionId)
            .orElseThrow(() -> new IllegalArgumentException("Election not found with id: " + electionId));

        election.setStatus(status);
        electionRepository.save(election);
    }

    public List<ElectionResponseDto> getElectionsByStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status parameter is required.");
        }


        return electionRepository.findByStatus(status).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public List<BaseDTO> getApprovedElections() {
        List<Election> approvedElections = electionRepository.findByStatusAndIsActiveTrue(Status.APPROVED.getDisplayName());

      return approvedElections.stream().filter(e->!e.getResultDate().isBefore(LocalDate.now()))
            .map(election -> new BaseDTO(election.getId(), election.getElectionName()))
            .toList();
    }

 private ElectionAddressDTO toDtoWithIdsDto(Election election) {
        return new ElectionAddressDTO(
            election.getCountry().getId(),
            election.getState().getId(),
             election.getCity().getId()
        );
    }
    private ElectionResponseDto toDto(Election election) {
        String countryName = countryService.getById(election.getCountry().getId()).getName();
        String stateName = stateService.getById(election.getState().getId()).getName();
        String cityName = cityService.getById( election.getCity().getId()).getName();
        String officerName = userDetailService.getUserById(election.getOfficer().getId()).getFullName();
        return new ElectionResponseDto(
            election.getId(),
            election.getElectionName(),
            election.getElectionDate(),
            election.getResultDate(),
            countryName,
            stateName,
             cityName,
            officerName,
            election.getStatus(),
            null, 
            election.getIsPublish(),
            election.getIsResultPublish()
        );
    }
    
    /**
     * Sends email notifications to eligible voters when an election is published
     */
    private void sendElectionPublishedNotification(Election election) throws IllegalArgumentException {
        try {
            // Get all active voters in the election's city
            List<UserDetail> eligibleVoters = userDetailRepository.findActiveVoters(election.getCity().getId());
            if(eligibleVoters.isEmpty()){
              log.info("No eligible voters found for election id: " + election.getId());
              throw new IllegalArgumentException("No eligible voters found for election id: " + election.getId());
            }
            List<CandidateVotingDetail> candidates = candidateService.getCandidateByElectionIdWithDetail(election.getId());
            if(candidates.isEmpty()){
              log.info("No candidates found for election id: " + election.getId());
              throw new IllegalArgumentException("No candidates found for election id: " + election.getId());
            }
            // Create email template data
            Map<String, Object> templateData = createElectionEmailTemplateData(election,candidates);
            
            // Send email to each eligible voter
            for (UserDetail voter : eligibleVoters) {
                try {
                    // Add personalized data for each voter
                    templateData.put("voterName", voter.getFirstName());
                    templateData.put("voterEmail", voter.getEmailId());
                    
                    emailService.sendEmailWithTemplate(
                        voter.getEmailId(),
                        EmailConstants.ELECTION_PUBLISHED_SUBJECT,
                        EmailConstants.ELECTION_PUBLISHED_TEMPLATE,
                        templateData
                    );
                    
                } catch (Exception e) {
                    // Log individual email failures but continue with others
                    System.err.println("Failed to send election notification email to " + voter.getEmailId() + ": " + e.getMessage());
                }
            }
            
            System.out.println("Election notification emails sent to " + eligibleVoters.size() + " eligible voters");
            
        } catch (Exception e) {
            // Log error but don't fail the election publication
            System.err.println("Failed to send election notification emails: " + e.getMessage());
        }
    }
    
    /**
     * Creates template data for election notification emails
     */
    private Map<String, Object> createElectionEmailTemplateData(Election election,List<CandidateVotingDetail> candidateVotingDetails) {
        Map<String, Object> templateData = new HashMap<>();
        
        // Election details
        templateData.put("electionName", election.getElectionName());
        templateData.put("electionDate", election.getElectionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        templateData.put("formEndDate", election.getFormEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        templateData.put("resultDate", election.getResultDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        // Location details
        String countryName = countryService.getById(election.getCountry().getId()).getName();
        String stateName = stateService.getById(election.getState().getId()).getName();
        String cityName = cityService.getById(election.getCity().getId()).getName();
        
        templateData.put("country", countryName);
        templateData.put("state", stateName);
        templateData.put("city", cityName);
        
        // Officer details
        String officerName = userDetailService.getUserById(election.getOfficer().getId()).getFullName();
        templateData.put("officerName", officerName);
        
        // Additional information
        templateData.put("status", election.getStatus());
        templateData.put("note", election.getNote());
        templateData.put("publishDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        templateData.put("candidates", candidateVotingDetails);
        return templateData;
    }

    public List<BaseDTO> getElectionToPublish() {

      List<Election> publishedElections = electionRepository.findByIsPublishTrueAndIsResultPublishFalseAndIsActiveTrue();

      return publishedElections.stream().filter(election-> election.getResultDate().equals(LocalDate.now()))
            .map(election -> new BaseDTO(election.getId(), election.getElectionName()))
            .toList();
    }

    public List<BaseDTO> getElectionsForShowResult() {
      List<Election> electionsForResult = electionRepository.findByIsPublishTrueAndIsResultPublishTrueAndIsActiveTrue();

      return electionsForResult.stream().map(election -> new BaseDTO(election.getId(), election.getElectionName()))
            .toList();
    }


    public List<ElectionResultDTO> publishElectionResult(Long electionId) {
        Election election = electionRepository.findById(electionId)
            .orElseThrow(() -> new IllegalArgumentException("Election not found with id: " + electionId));

        List<Candidate> candidates = candidateService.getCandidateEntityByElectionId(electionId);

        long totalVotes = votingService.getTotalVotesByElectionId(electionId);

        List<ElectionResultDTO> results = candidates.stream().map(candidate -> {
            Long votesReceived = votingService.getVotesForCandidateInElection(candidate.getId(), electionId);
            ElectionResultDTO resultDTO = new ElectionResultDTO();
            resultDTO.setCandidateId(candidate.getId());
            resultDTO.setCandidateName(candidate.getFirstName() + " " +candidate.getMiddleName() + " " + candidate.getLastName());
            resultDTO.setFirstName(candidate.getFirstName());
            resultDTO.setLastName(candidate.getLastName());
            resultDTO.setMiddleName(candidate.getMiddleName());
            resultDTO.setVotes(votesReceived);
            resultDTO.setPartyName(candidate.getParty().getName());
            resultDTO.setCandidateImageUrl(candidate.getCandidatePhoto());
            resultDTO.setPartyImageUrl(candidate.getParty().getLogoUrl());
            if (totalVotes > 0) {
                double percentage = (votesReceived.doubleValue() / totalVotes) * 100;
                resultDTO.setPercentage(Math.round(percentage * 100.0) / 100.0); // Round to 2 decimal places
            } else {
                resultDTO.setPercentage(0.0);
            }

               // Save election results to database
        ElectionResult electionResult = new ElectionResult();
        electionResult.setElection(election);
        electionResult.setCandidate(candidate);
        electionResult.setParty(candidate.getParty());
        electionResult.setVotesReceived(votesReceived);
        electionResult.setTotalElectionVoted(totalVotes);
        electionResult.setActive(true);
        electionResult.setCreatedBy("system");
        electionResult.setUpdatedDate(java.time.LocalDateTime.now());
        electionResult.setUpdateBy("system");
        electionResult.setCreatedDate(java.time.LocalDateTime.now());
        electionResultRepository.save(electionResult);
            return resultDTO;
        }).collect(Collectors.toList());

        // Determine winner
        Long maxVotes = results.stream()
            .mapToLong(ElectionResultDTO::getVotes)
            .max()
            .orElse(0L);

        results.forEach(result -> {
            if (result.getVotes().equals(maxVotes) && maxVotes > 0) {
                result.setIsWinner(true);
            } else {
                result.setIsWinner(false);
            }
        });

        // Update election to mark results as published
        election.setIsResultPublish(true);
        electionRepository.save(election);


      

     
        return results;
        
    }   


    // TO GET THE RESULT
    public ElectionResultMainDTO getElectionResult(Long electionId) {
        List<ElectionResultDTO> resultList = new ArrayList<>();

        List<ElectionResult> electionResults = electionResultRepository.findByElectionId(electionId);
        Election election = electionRepository.findById(electionId)
            .orElseThrow(() -> new IllegalArgumentException("Election not found with id: " + electionId));
            
        resultList = electionResults.stream().map(result -> {
            ElectionResultDTO resultDTO = new ElectionResultDTO();
            resultDTO.setCandidateId(result.getCandidate().getId());
            resultDTO.setCandidateName(result.getCandidate().getFirstName() + " " +result.getCandidate().getMiddleName() + " " + result.getCandidate().getLastName());
            resultDTO.setFirstName(result.getCandidate().getFirstName());
            resultDTO.setLastName(result.getCandidate().getLastName());
            resultDTO.setMiddleName(result.getCandidate().getMiddleName());
            resultDTO.setVotes(result.getVotesReceived());
            resultDTO.setPartyName(result.getParty().getName());
            resultDTO.setCandidateImageUrl(result.getCandidate().getCandidatePhoto());
            resultDTO.setPartyImageUrl(result.getParty().getLogoUrl());
            if (result.getTotalElectionVoted() > 0) {
                double percentage = (result.getVotesReceived().doubleValue() / result.getTotalElectionVoted()) * 100;
                resultDTO.setPercentage(Math.round(percentage * 100.0) / 100.0); // Round to 2 decimal places
            } else {
                resultDTO.setPercentage(0.0);
            }
            return resultDTO;
        }).collect(Collectors.toList());

        ElectionResultMainDTO electionResultMainDTO = new ElectionResultMainDTO();
        electionResultMainDTO.setElectionName(election.getElectionName());
        electionResultMainDTO.setElectionDate(election.getElectionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        electionResultMainDTO.setElectionResultDate(election.getResultDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        electionResultMainDTO.setElectionResults(resultList);
        return electionResultMainDTO;
    }   

   public ElectionDataPoint getElectionDataPoint() {
        Long totalElections = electionRepository.count();
        Long totalElectionApproved = electionRepository.findByStatus(Status.APPROVED.getDisplayName()).stream().count();
        Long totalElectionunApproved = totalElections - totalElectionApproved;
        Long totalElectionResultPublished = electionRepository.findByIsPublishTrueAndIsResultPublishTrueAndIsActiveTrue().stream().count();
        Long totalElectionResultUnPublished = electionRepository.findByIsPublishTrueAndIsResultPublishFalseAndIsActiveTrue().stream().count();

        return new ElectionDataPoint(
            totalElections,
            totalElectionApproved,
            totalElectionResultPublished,
            totalElectionResultUnPublished,
            totalElectionunApproved
        );
    }
}

