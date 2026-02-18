package com.onlinevoting.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.onlinevoting.model.UserDetail;
import com.onlinevoting.service.JwtService;
import com.onlinevoting.service.UserDetailService;
import com.onlinevoting.util.UserContextHelper;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailService userDetailsService;
    private final UserContextHelper userContextHelper;
    private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailService userDetailsService, UserContextHelper userContextHelper) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userContextHelper = userContextHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestPath = request.getRequestURI();
        
        // Skip JWT validation for public endpoints
        if (requestPath.contains("/v1/user/generate_otp") ||
         requestPath.contains("/v1/user/validate_otp") ||
         requestPath.contains("/v1/cities/by-state/") ||
         requestPath.contains("/v1/address/") ||
         requestPath.contains("/v1/country/list") ||
         requestPath.contains("/v1/states/by-country/") ||
         requestPath.contains("/v1/roles/") ||
         requestPath.contains("/v1/address/") ||
         requestPath.contains("/v1/user_detail") ||
         requestPath.contains("/v1/voter/search")
        ) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT Token");
            response.getWriter().flush();
            return;
        }

        String jwt = authHeader.substring(7);
        String username;
        try
        {
            username = jwtService.extractUsername(jwt);
        }
        catch (Exception e)
        {
            logger.error("JWT extraction error: {}", e.getMessage());
            // thriow 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT Token");
            response.getWriter().flush();
            return;
        }
        

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetail userDetails = userDetailsService.getUserByEmail(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Set user context for this request
                userContextHelper.setCurrentUserEmail(username);
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, null);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Clear user context after request completion
            userContextHelper.clearUserContext();
        }
    }
}