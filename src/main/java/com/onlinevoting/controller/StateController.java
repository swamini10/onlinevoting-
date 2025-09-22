package com.onlinevoting.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinevoting.dto.ApiResponse;
import com.onlinevoting.dto.StateDTO;
import com.onlinevoting.service.StateService;

@RestController
@RequestMapping("/v1/state")    
public class StateController {

    private final StateService stateService;

    public StateController(StateService stateService) {
        this.stateService = stateService;
    }
    
    @GetMapping(path = "/list", produces = { "application/json" })
    public ResponseEntity<ApiResponse<List<StateDTO>>> getStates() {
        return ResponseEntity.ok(new ApiResponse<>(true, stateService.getAll(), null));
    }

}
