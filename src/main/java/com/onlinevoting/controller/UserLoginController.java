package com.onlinevoting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.onlinevoting.dto.ApiResponse;
import com.onlinevoting.dto.UserLoginDTO;
import com.onlinevoting.dto.UserLoginInfo;
import com.onlinevoting.service.JwtService;
import com.onlinevoting.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserLoginController {

    private final LoginService loginService;
    private final JwtService jwtService;

    public UserLoginController(@Autowired LoginService loginService, @Autowired JwtService jwtService) {
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    @PostMapping("/v1/user/generate_otp")
    public ResponseEntity<ApiResponse> genrateOtp(@RequestBody @Valid UserLoginInfo userLoginInfo) {
        loginService.generateOtp(userLoginInfo);
        return ResponseEntity.ok(new ApiResponse<>(true, "",null,"OTP Generate Successfully"));
    }

    @PostMapping("/v1/user/validate_otp") 
    public ResponseEntity<ApiResponse> loginUser(@RequestBody @Valid UserLoginDTO userLoginInfDto) {
        Boolean isLoginSuccess = loginService.loginUser(userLoginInfDto);
        if(isLoginSuccess) {
            String token = jwtService.generateToken(userLoginInfDto.getUserId());
            return ResponseEntity.ok(new ApiResponse<>(isLoginSuccess, token ,null,"Login Successfully..."));
        }
        return ResponseEntity.ok(new ApiResponse<>(isLoginSuccess,"" ,null,"Login Failed"));
    }
}
