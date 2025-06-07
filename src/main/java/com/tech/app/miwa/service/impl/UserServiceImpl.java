package com.tech.app.miwa.service.impl;

import com.tech.app.miwa.model.User;
import com.tech.app.miwa.repository.UserRepository;
import com.tech.app.miwa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean login(String username, String password) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        return authentication.isAuthenticated();
    }

    @Override
    public User register(String username, String password) throws Exception {
        User user = User.builder()
                .username(username)
                .password(encodePassword(password))
                .build();
        return userRepository.save(user);
    }

    @Override
    public void deleteAccountUser(Long userId) throws Exception {
        userRepository.deleteById(userId);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
