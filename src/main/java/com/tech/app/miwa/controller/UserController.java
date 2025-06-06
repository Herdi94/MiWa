package com.tech.app.miwa.controller;

import com.tech.app.miwa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        try {
            boolean auth = userService.login(username, password);
            return ResponseEntity.ok("Login Status "+(auth ? "Logged In" : "Not Logged In"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid username and password!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error : "+e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username, @RequestParam String password) {
        try{
            userService.register(username, password);
            return ResponseEntity.ok("Successfully created new user");
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save user - username already exist.");
        }catch (TransactionSystemException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save user - validation error");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error : "+e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccountUser(@PathVariable("id") Long userId) {
        try{
            userService.deleteAccountUser(userId);
            return ResponseEntity.ok("Successfully deleted this user");
        }catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error : "+e.getMessage());
        }
    }
}
