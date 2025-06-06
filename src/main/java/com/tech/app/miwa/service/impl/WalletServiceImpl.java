package com.tech.app.miwa.service.impl;

import com.tech.app.miwa.model.TransactionType;
import com.tech.app.miwa.model.User;
import com.tech.app.miwa.model.Wallet;
import com.tech.app.miwa.repository.TransactionRepository;
import com.tech.app.miwa.repository.UserRepository;
import com.tech.app.miwa.repository.WalletRepository;
import com.tech.app.miwa.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void topUp(Long walletId, BigDecimal amount) throws Exception {
        Wallet wallet = findWalletById(walletId);
        updateWalletBalance(wallet, amount, TransactionType.TOPUP);
    }

    @Override
    public void transfer(Long sourceWalletId, Long targetWalletId, BigDecimal amount) throws Exception {
        Wallet sourceWallet = findWalletById(sourceWalletId);
        Wallet targetWallet = findWalletById(targetWalletId);

        if (sourceWallet.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance");

        updateWalletBalance(sourceWallet, amount, TransactionType.TRANSFER);
        updateWalletBalance(targetWallet, amount, TransactionType.TOPUP);
    }

    @Override
    public void createAccountWallet(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        Wallet wallet = Wallet.builder()
                .name(user.getUsername())
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();
        walletRepo.save(wallet);
    }

    @Override
    public void deleteAccountWallet(Long walletId) throws Exception {
        walletRepo.deleteById(walletId);
    }

    @Override
    public List<Wallet> otherWallets(Long walletId) throws Exception {
        return walletRepo.findOtherById(walletId);
    }

    private Wallet findWalletById(Long walletId){
        return walletRepo.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    private void updateWalletBalance(Wallet wallet, BigDecimal amount, TransactionType transactionType){
        BigDecimal balance = BigDecimal.ZERO;

        if(transactionType.equals(TransactionType.TOPUP))
            balance = wallet.getBalance().add(amount);
        else
            balance = wallet.getBalance().subtract(amount);

        wallet.setBalance(balance);
        walletRepo.save(wallet);
    }
}
