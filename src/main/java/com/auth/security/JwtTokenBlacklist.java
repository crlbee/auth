package com.auth.security;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenBlacklist {

    private final Set<String> tokenBlacklist = new HashSet<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private static final long TOKEN_LIFETIME_MINUTES = 15;

    public void addToTokenBlacklist(String token) {
        tokenBlacklist.add(token);
        scheduleTokenRemoval(token);
    }

    public boolean containsToken(String token) {
        return tokenBlacklist.contains(token);
    }

    private void scheduleTokenRemoval(String token) {
        executorService.schedule(() -> removeTokenFromBlacklist(token), TOKEN_LIFETIME_MINUTES, TimeUnit.MINUTES);
    }

    private void removeTokenFromBlacklist(String token) {
        tokenBlacklist.remove(token);
    }

}
