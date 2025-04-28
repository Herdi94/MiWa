package com.tech.app.miwa.api;

import com.tech.app.miwa.model.User;
import com.tech.app.miwa.repository.UserRepository;
import com.tech.app.miwa.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class AuthAPI {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            return ResponseEntity.ok("Status Login "+(authentication.isAuthenticated() ? "Berhasil" : "Tidak Berhasil"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid username and password!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username, @RequestParam String password) {

        try{
            User user = new User();
            user.setUsername(username);
            user.setPassword(authService.encodePassword(password));
            userRepository.save(user);
            return ResponseEntity.ok("Successfully created new account");
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save user - username already exist.");
        }catch (TransactionSystemException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save user - validation error");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error : "+e.getMessage());
        }

    }
}
