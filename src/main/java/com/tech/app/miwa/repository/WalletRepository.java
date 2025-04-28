package com.tech.app.miwa.repository;

import com.tech.app.miwa.model.User;
import com.tech.app.miwa.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByUser(User user);
}
