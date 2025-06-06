package com.tech.app.miwa.service;

public interface UserService {
    boolean login(String username, String password) throws Exception;
    void register(String username, String password) throws Exception;
    void deleteAccountUser(Long userId) throws Exception;
}
