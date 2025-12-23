package com.onlinevoting.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onlinevoting.dto.ApiResponse;
import com.onlinevoting.model.Election;
import com.onlinevoting.service.ElectionService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ElectionController {

    private final ElectionService electionService;

    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }
    
    @PostMapping(path="/v1/election", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<Object>> createElection(@RequestBody @Valid String election) {
        electionService.saveElection(election);
        ApiResponse<Object> response = new ApiResponse<>(true, "Election created successfully", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
