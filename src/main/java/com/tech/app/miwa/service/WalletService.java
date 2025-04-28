package com.tech.app.miwa.service;

import com.tech.app.miwa.model.Transaction;
import com.tech.app.miwa.model.Wallet;
import com.tech.app.miwa.repository.TransactionRepository;
import com.tech.app.miwa.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    public void topUp(Long walletId, double amount) {
        Wallet wallet = walletRepo.findById(walletId).orElseThrow();
        wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(amount)));
        walletRepo.save(wallet);

        Transaction trx = new Transaction();
        trx.setType("TOPUP");
        trx.setAmount(amount);
        trx.setSourceWallet(wallet);
        trx.setTimestamp(LocalDateTime.now());
        trx.setDescription("Top up");
        transactionRepo.save(trx);
    }

    public void transfer(Long fromWalletId, Long toWalletId, double amount) {
        Wallet from = walletRepo.findById(fromWalletId).orElseThrow();
        Wallet to = walletRepo.findById(toWalletId).orElseThrow();

        if (from.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0)
            throw new RuntimeException("Insufficient balance");

        from.setBalance(from.getBalance().subtract(BigDecimal.valueOf(amount)));
        to.setBalance(to.getBalance().add(BigDecimal.valueOf(amount)));
        walletRepo.save(from);
        walletRepo.save(to);

        Transaction t = new Transaction();
        t.setType("TRANSFER");
        t.setAmount(amount);
        t.setSourceWallet(from);
        t.setTargetWallet(to);
        t.setTimestamp(LocalDateTime.now());
        t.setDescription("Transfer from " + from.getName() + " to " + to.getName());
        transactionRepo.save(t);
    }
}
