package com.onlinevoting.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinevoting.dto.ApiResponse;
import com.onlinevoting.dto.BaseDTO;
import com.onlinevoting.service.UserRoleService;

@RestController
@RequestMapping("/v1/roles")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserRoleService userRoleService;  

    public UserController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

     @GetMapping(path = "/", produces = { "application/json" })
    public ResponseEntity<ApiResponse<List<BaseDTO>>> getAllUserRoles() {
        List<BaseDTO> roles = userRoleService.getAllUserRoles();
        return ResponseEntity.ok(new ApiResponse<>(true, roles, null));
    }
    
}
