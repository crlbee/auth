package com.auth.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BasicLogoutHandler implements LogoutHandler {
    private final JwtTokenBlacklist jwtTokenBlacklist;

    public BasicLogoutHandler(JwtTokenBlacklist jwtTokenBlacklist) {
        this.jwtTokenBlacklist = jwtTokenBlacklist;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            jwtTokenBlacklist.addToTokenBlacklist(token);
        }
        SecurityContextHolder.clearContext();
    }
}
