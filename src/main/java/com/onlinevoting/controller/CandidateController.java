package com.onlinevoting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlinevoting.dto.ApiResponse;
import com.onlinevoting.dto.CandidateResponseDTO;
import com.onlinevoting.dto.StatusRequestDTO;
import com.onlinevoting.dto.StatusUpdateRequestDTO;
import com.onlinevoting.service.CandidateService;

import jakarta.validation.Valid;



@RestController
@CrossOrigin(origins = "", allowedHeaders = "")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping(path="/v1/candidate_form", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<Object>> createCandidate(@RequestBody @Valid String candidate) {
        candidateService.saveCandidate(candidate);
        ApiResponse<Object> response = new ApiResponse<>(true, "Candidate created successfully", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(path = "/v1/candidate_form/findbystatus", produces = { "application/json" })
    public ResponseEntity<ApiResponse<List<CandidateResponseDTO>>> getCandidatesByStatus(@RequestParam String status) {
        List<CandidateResponseDTO> candidates = candidateService.getCandidatesByStatus(status);
        ApiResponse<List<CandidateResponseDTO>> response = new ApiResponse<>(true, candidates, null);
        return ResponseEntity.ok(response);
    }
   @PatchMapping(path = "/v1/candidate/updatestatus/{candidateId}", consumes = { "application/json" }, produces = { "application/json" }  )
    public ResponseEntity<ApiResponse<String>> approveUser(@PathVariable Long candidateId, 
        @RequestBody StatusRequestDTO statusUpdateRequest ) {
        candidateService.updateStatusOfCandidate(candidateId, statusUpdateRequest.getStatus(),statusUpdateRequest.getNoteForStatus()    );
        ApiResponse<String> response = new ApiResponse<>(true, "Election " + statusUpdateRequest.getStatus().toLowerCase() + " successfully", null);
        return ResponseEntity.ok(response);
    }
    @GetMapping(path = "/v1/candidate/approved", produces = { "application/json"})
    public ResponseEntity<ApiResponse<List<CandidateResponseDTO>>> getApprovedCandidate() {
        List<CandidateResponseDTO> candidates = candidateService.getCandidatesByStatus("APPROVED");
        ApiResponse<List<CandidateResponseDTO>> response = new ApiResponse<>(true, candidates, null);
        return ResponseEntity.ok(response);
    }
}

