package com.auth.service;

import com.auth.entity.User;
import com.auth.exception.UserNotFoundException;
import com.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

@Service("userDetailsService")
public class BasicUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked()) {
            throw new RuntimeException("blocked");
        }

        try {
            Optional<User> user = userRepository.findByName(name);
            if (user.isEmpty()) {
                return new org.springframework.security.core.userdetails.User(
                        " ", " ", true, true, true, true,
                        Collections.singleton(new SimpleGrantedAuthority("USER")));
            }
            return new org.springframework.security.core.userdetails.User(
                    user.get().getName(), user.get().getPassword(), user.get().isEnabled(), true, true, true,
                    Collections.singleton(new SimpleGrantedAuthority("USER")));
        } catch (Exception e) {
            throw new UserNotFoundException(e.toString());
        }
    }
}
