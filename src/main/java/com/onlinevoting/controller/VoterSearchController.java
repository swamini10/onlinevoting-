package com.onlinevoting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlinevoting.dto.ApiResponse;
import com.onlinevoting.service.VoterSearchService;

@RestController
@CrossOrigin(origins = "*")
public class VoterSearchController {

    private final VoterSearchService voterSearchService;
    
    public VoterSearchController(VoterSearchService voterSearchService) {
        this.voterSearchService = voterSearchService;
    }

    @GetMapping(path = "/v1/voter/search", produces = "application/json")
    public ResponseEntity<ApiResponse> searchVoter(
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email) {
        
        // Validate that at least one parameter is provided
        if ((phone == null || phone.trim().isEmpty()) && (email == null || email.trim().isEmpty())) {
            return ResponseEntity.badRequest().body(
                new ApiResponse(false, null, null, "At least one search parameter (phone or email) must be provided."));
        }
        if(phone != null && !phone.trim().isEmpty()) {
           return ResponseEntity.ok(new ApiResponse(true, 
            voterSearchService.searchVoters("phone", phone), null));
        } 
        return ResponseEntity.ok(new ApiResponse(true, 
            voterSearchService.searchVoters("email", email), null));      
    }
}
