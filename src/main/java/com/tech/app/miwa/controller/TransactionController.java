package com.tech.app.miwa.controller;

import com.tech.app.miwa.model.Transaction;
import com.tech.app.miwa.repository.TransactionRepository;
import com.tech.app.miwa.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/api/transaction")
@RestController
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/topup-trx")
    public ResponseEntity<?> topUpTrx(@RequestParam Long walletId, @RequestParam BigDecimal amount) {
        try {
            transactionService.topUpTrx(walletId, amount);
            return ResponseEntity.ok("Top-up transaction successfull");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error : "+e.getMessage());
        }
    }

    @PostMapping("/transfer-trx")
    public ResponseEntity<?> transferTrx(@RequestParam Long sourceWalletId, @RequestParam Long targetWalletId, @RequestParam BigDecimal amount) {
        try {
            transactionService.transferTrx(sourceWalletId, targetWalletId, amount);
            return ResponseEntity.ok("Transfer transaction successfull");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error : "+e.getMessage());
        }
    }

    @GetMapping("/history-trx")
    public ResponseEntity<?> historyTrx(@RequestParam Long walletId) {
        try {
            List<Transaction> transactions = transactionService.historyTrx(walletId);
            return ResponseEntity.ok(transactions);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error : "+e.getMessage());
        }
    }
}
