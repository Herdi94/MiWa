package com.tech.app.miwa.service;

import com.tech.app.miwa.model.User;
import com.tech.app.miwa.model.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {
    void topUp(Long walletId, BigDecimal amount) throws Exception;
    void transfer(Long sourceWalletId, Long targetWalletId, BigDecimal amount) throws Exception;
    void createAccountWallet(Long userId) throws Exception;
    void deleteAccountWallet(Long walletId) throws Exception;
    List<Wallet> otherWallets(Long walletId) throws Exception;
    Wallet getWallet(String username) throws Exception;
}
