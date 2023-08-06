package com.auth.security;

public interface JWTTokenProvider {
    String generateToken(String userDetails);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    String extractTokenFromHeader(String authorizationHeader);
}
