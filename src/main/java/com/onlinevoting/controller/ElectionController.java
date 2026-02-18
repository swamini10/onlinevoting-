package com.onlinevoting.controller;

import java.util.List;

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
import com.onlinevoting.dto.BaseDTO;
import com.onlinevoting.dto.ElectionAddressDTO;
import com.onlinevoting.dto.ElectionDataPoint;
import com.onlinevoting.dto.ElectionResponseDto;
import com.onlinevoting.dto.ElectionResultDTO;
import com.onlinevoting.dto.ElectionResultMainDTO;
import com.onlinevoting.dto.PublishResultRequest;
import com.onlinevoting.dto.StatusUpdateRequestDTO;
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

    @GetMapping(path="/v1/election/{electionId}", produces = "application/json")
    public ResponseEntity<ApiResponse<ElectionAddressDTO>> getElectionById(@PathVariable Long  electionId) {
        ElectionAddressDTO election = electionService.getElectionById(electionId);
        ApiResponse<ElectionAddressDTO> response = new ApiResponse<>(true, election, null);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path="/v1/election/detail/{electionId}", produces = "application/json")
    public ResponseEntity<ApiResponse<ElectionResponseDto>> getElectionDetails(@PathVariable Long  electionId) {
        ElectionResponseDto election = electionService.getElectionDetails(electionId);
        ApiResponse<ElectionResponseDto> response = new ApiResponse<>(true, election, null);
        return ResponseEntity.ok(response);
    }


    @GetMapping(path="/v1/election", produces = "application/json")
    public ResponseEntity<ApiResponse<List<ElectionResponseDto>>> getAllElections() {
        List<ElectionResponseDto> elections = electionService.getAllElections();
        ApiResponse<List<ElectionResponseDto>> response = new ApiResponse<>(true, elections, null);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/v1/election/findbystatus", produces = { "application/json"})
    public ResponseEntity<ApiResponse<List<ElectionResponseDto>>> getElectionsByStatus(
        @RequestParam String status) {
        List<ElectionResponseDto> elections = electionService.getElectionsByStatus(status);
        ApiResponse<List<ElectionResponseDto>> response = new ApiResponse<>(true, elections, null);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/v1/election/approved", produces = { "application/json"})
    public ResponseEntity<ApiResponse<List<BaseDTO>>> getApprovedElections() {
        List<BaseDTO> elections = electionService.getApprovedElections();
        ApiResponse<List<BaseDTO>> response = new ApiResponse<>(true, elections, null);
        return ResponseEntity.ok(response);
    }


    @PatchMapping(path = "/v1/election/status/{electionId}", consumes = { "application/json" }, produces = { "application/json" }  )
    public ResponseEntity<ApiResponse<String>> approveUser(@PathVariable Long electionId, 
        @RequestBody StatusUpdateRequestDTO statusUpdateRequest ) {
        electionService.approveElection(electionId, statusUpdateRequest.getStatus());
        ApiResponse<String> response = new ApiResponse<>(true, "Election " + statusUpdateRequest.getStatus().toLowerCase() + " successfully", null);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/v1/election/publish/{electionId}", consumes = { "application/json" }, produces = { "application/json" }  )
    public ResponseEntity<ApiResponse<String>> publishElection(@PathVariable Long electionId, 
        @RequestBody StatusUpdateRequestDTO statusUpdateRequest ) {
        electionService.publishElection(electionId,statusUpdateRequest);
        ApiResponse<String> response = new ApiResponse<>(true, "Election published successfully", null);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping(path="/v1/election/notification/{electionId}", produces = "application/json")
    public ResponseEntity<ApiResponse<String>> sendElectionNotification(@PathVariable Long  electionId) {
        electionService.sendElectionNotification(electionId);
        ApiResponse<String> response = new ApiResponse<>(true, "Election notification sent successfully", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path="/v1/election/publish_election_result", produces = "application/json")
    public ResponseEntity<ApiResponse<List<ElectionResultDTO>>> publishResult(@RequestBody PublishResultRequest publishResultRequest) {   
        List<ElectionResultDTO> elections = electionService.publishElectionResult(publishResultRequest.getElectionId());
        ApiResponse<List<ElectionResultDTO>> response = new ApiResponse<>(true, elections, null);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping(path="/v1/election/getElectionForResult", produces = "application/json")
    public ResponseEntity<ApiResponse<List<BaseDTO>>> getElectionsForResult() {   
        List<BaseDTO> elections = electionService.getElectionToPublish();
        ApiResponse<List<BaseDTO>> response = new ApiResponse<>(true, elections, null);
        return ResponseEntity.ok(response);
    }




    @GetMapping(path="/v1/election/getElectionsForShowResult", produces = "application/json")
    public ResponseEntity<ApiResponse<List<BaseDTO>>> getElectionsForShowResult() {
         List<BaseDTO> elections  = electionService.getElectionsForShowResult();
        ApiResponse<List<BaseDTO>> response = new ApiResponse<>(true, elections, null);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path="/v1/election/datapoint", produces = "application/json")
    public ResponseEntity<ApiResponse<ElectionDataPoint>> getElectionDataPoint() {
        ElectionDataPoint dataPoints = electionService.getElectionDataPoint();
        ApiResponse<ElectionDataPoint> response = new ApiResponse<>(true, dataPoints, null);
        return ResponseEntity.ok(response);
    }
<<<<<<< HEAD
}
=======

    @GetMapping(path="/v1/election/getResult/{electionId}", produces = "application/json")
    public ResponseEntity<ApiResponse<ElectionResultMainDTO>> getResult(@PathVariable Long electionId) {   
        ElectionResultMainDTO electionResult = electionService.getElectionResult(electionId);
        ApiResponse<ElectionResultMainDTO> response = new ApiResponse<>(true, electionResult, null);
        return ResponseEntity.ok(response);
    }

}
>>>>>>> 383b2ca030343cdf2e47b11ec16e4c73f95ef0af
