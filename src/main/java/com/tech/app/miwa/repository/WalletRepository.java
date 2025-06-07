package com.tech.app.miwa.repository;

import com.tech.app.miwa.model.User;
import com.tech.app.miwa.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByUserId(Long userId);

    @Query(value = "SELECT w.* FROM wallet w WHERE w.id <> :walletId", nativeQuery = true)
    List<Wallet> findOtherById(Long walletId);
}
