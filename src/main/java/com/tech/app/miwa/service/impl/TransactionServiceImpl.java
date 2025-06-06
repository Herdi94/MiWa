package com.tech.app.miwa.service.impl;

import com.tech.app.miwa.model.Transaction;
import com.tech.app.miwa.model.TransactionType;
import com.tech.app.miwa.model.Wallet;
import com.tech.app.miwa.repository.TransactionRepository;
import com.tech.app.miwa.repository.WalletRepository;
import com.tech.app.miwa.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public void topUpTrx(Long walletId, BigDecimal amount) throws Exception {
        Wallet wallet = findWalletById(walletId);

        Transaction trx = Transaction.builder()
                .type(TransactionType.TOPUP)
                .amount(amount)
                .sourceWallet(wallet)
                .timestamp(LocalDateTime.now())
                .description("TOPUP")
                .build();
        transactionRepository.save(trx);
    }

    @Override
    public void transferTrx(Long sourceWalletId, Long targetWalletId, BigDecimal amount) throws Exception {
        Wallet sourceWallet = findWalletById(sourceWalletId);
        Wallet targetWallet = findWalletById(targetWalletId);

        Transaction trx = Transaction.builder()
                .type(TransactionType.TRANSFER)
                .amount(amount)
                .sourceWallet(sourceWallet)
                .targetWallet(targetWallet)
                .timestamp(LocalDateTime.now())
                .description("Transfer from " +sourceWallet.getName() + " to " +targetWallet.getName())
                .build();
        transactionRepository.save(trx);
    }

    @Override
    public List<Transaction> historyTrx(Long walletId) throws Exception {
        Wallet wallet = findWalletById(walletId);
        return transactionRepository.findBySourceWalletOrTargetWallet(wallet, wallet);
    }

    private Wallet findWalletById(Long walletId){
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }
}
