package com.onlinevoting.service;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.onlinevoting.enums.Status;
import com.onlinevoting.model.Election;
import com.onlinevoting.repository.ElectionRepository;

@Service
public class ElectionService {

    private final ElectionRepository electionRepository;
    private final ObjectMapper objectMapper;

    public ElectionService(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository;
        this.objectMapper = new ObjectMapper();
        // Configure ObjectMapper to handle LocalDate properly
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    public Election saveElection(String election) {
        try {
            // Convert JSON string to Election object
            Election electionObject = objectMapper.readValue(election, Election.class);
            electionObject.setActive(true);
            electionObject.setStatus(Status.PENDING.getDisplayName());
            // Save the election object to database
            return electionRepository.save(electionObject);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse election JSON: " + e.getMessage(), e);
        }
    } 
}