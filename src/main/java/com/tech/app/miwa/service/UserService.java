package com.tech.app.miwa.service;

import com.tech.app.miwa.model.User;

public interface UserService {
    boolean login(String username, String password) throws Exception;
    User register(String username, String password) throws Exception;
    void deleteAccountUser(Long userId) throws Exception;
}
