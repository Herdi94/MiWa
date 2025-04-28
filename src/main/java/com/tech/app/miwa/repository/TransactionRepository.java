package com.tech.app.miwa.repository;

import com.tech.app.miwa.model.Transaction;
import com.tech.app.miwa.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceWalletOrTargetWallet(Wallet source, Wallet target);
}
