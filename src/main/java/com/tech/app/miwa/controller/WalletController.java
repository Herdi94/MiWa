package com.tech.app.miwa.controller;

import com.tech.app.miwa.model.Transaction;
import com.tech.app.miwa.model.User;
import com.tech.app.miwa.model.Wallet;
import com.tech.app.miwa.service.WalletService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/api/wallet")
@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/topup")
    public ResponseEntity<?> topup(@RequestParam Long walletId, @RequestParam BigDecimal amount) {
        try {
            walletService.topUp(walletId, amount);
            return ResponseEntity.ok("Top-up successfull");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error : "+e.getMessage());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam Long sourceWalletId, @RequestParam Long targetWalletId, @RequestParam BigDecimal amount) {
        try {
            walletService.transfer(sourceWalletId, targetWalletId, amount);
            return ResponseEntity.ok("Transfer successfull");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error : "+e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccountWallet(@PathVariable("id") Long walletId) {
        try{
            walletService.deleteAccountWallet(walletId);
            return ResponseEntity.ok("Successfully deleted this wallet account");
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("wallet account not found");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error : "+e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccountWallet(@RequestParam Long userId) {
        try{
            walletService.createAccountWallet(userId);
            return ResponseEntity.ok("Successfully created new wallet account");
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save wallet account - already exist.");
        }catch (TransactionSystemException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save wallet account - validation error");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error : "+e.getMessage());
        }
    }

    @GetMapping("/other-wallet")
    public ResponseEntity<?> otherWallet(@RequestParam Long walletId) {
        try {
            List<Wallet> otherWallets = walletService.otherWallets(walletId);
            return ResponseEntity.ok(otherWallets);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error : "+e.getMessage());
        }
    }
}
