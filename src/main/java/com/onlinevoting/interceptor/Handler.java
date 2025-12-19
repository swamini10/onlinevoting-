package com.onlinevoting.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.onlinevoting.service.JwtService;
import com.onlinevoting.service.UserDetailService;

import java.io.IOException;
import java.util.Set;

@Component
public class Handler implements HandlerInterceptor {

    private final JwtService jwtService;
    private final UserDetailService userDetailService;

    @Autowired
    public Handler(JwtService jwtService, UserDetailService userDetailService) {
        this.jwtService = jwtService;
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String requestPath = request.getRequestURI();

        // Bypass token validation for public endpoints and preflight requests
        Set<String> exactPublicEndpoints = Set.of("/v1/user/generate_otp", "/v1/user/validate_otp",
             "/v1/roles/", "/v1/address", "/v1/country/list"
        );
        
        Set<String> patternPublicEndpoints = Set.of("/v1/states/by-country/", "/v1/country/", 
             "/v1/states/", "/v1/cities/", "/v1/roles/"
        );
        
        boolean isPublicEndpoint = exactPublicEndpoints.contains(requestPath) ||
            patternPublicEndpoints.stream().anyMatch(pattern -> requestPath.startsWith(pattern));
            
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || isPublicEndpoint) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorized(response, "Missing or invalid Authorization header");
            return false;
        }

        String token = authHeader.substring(7).trim();
        if (token.isEmpty()) {
            sendUnauthorized(response, "Empty token");
            return false;
        }

        try {
            String username = jwtService.extractUsername(token);
            if (username == null || username.isEmpty()) {
                sendUnauthorized(response, "Invalid token");
                return false;
            }
        } catch (Exception e) {
            sendUnauthorized(response, "Invalid token: " + e.getMessage());
            return false;
        }
        return true;
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
        response.getWriter().flush();
    }
}
