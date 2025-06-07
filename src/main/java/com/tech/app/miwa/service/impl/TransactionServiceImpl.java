package com.tech.app.miwa.service.impl;

import com.tech.app.miwa.model.Transaction;
import com.tech.app.miwa.model.TransactionType;
import com.tech.app.miwa.model.Wallet;
import com.tech.app.miwa.repository.TransactionRepository;
import com.tech.app.miwa.repository.WalletRepository;
import com.tech.app.miwa.service.TransactionService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

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

    @Override
    public ByteArrayInputStream  exportToExcel(Long walletId) throws Exception {
        List<Transaction> transactions = historyTrx(walletId);

        String[] columns = {"ID", "Type", "Amount", "Source Wallet", "Target Wallet", "Date"};

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("transaction_history");

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy, h:mm:ss a", Locale.US);

            int rowIdx = 1;
            int index = 1;
            for (Transaction trx : transactions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(index++);
                row.createCell(1).setCellValue(trx.getType().toString());
                row.createCell(2).setCellValue(trx.getAmount().doubleValue());
                row.createCell(3).setCellValue(trx.getSourceWallet() != null ? trx.getSourceWallet().getName() : "-");
                row.createCell(4).setCellValue(trx.getTargetWallet() != null ? trx.getTargetWallet().getName() : "-");
                row.createCell(5).setCellValue(trx.getTimestamp().format(formatter));
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private Wallet findWalletById(Long walletId){
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }
}
