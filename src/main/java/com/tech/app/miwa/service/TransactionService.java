package com.tech.app.miwa.service;

import com.tech.app.miwa.model.Transaction;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    void topUpTrx(Long walletId, BigDecimal amount) throws Exception;
    void transferTrx(Long sourceWalletId, Long targetWalletId, BigDecimal amount) throws Exception;
    List<Transaction> historyTrx(Long walletId) throws Exception;
    ByteArrayInputStream exportToExcel(Long walletId) throws Exception;
}
