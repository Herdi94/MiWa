package com.tech.app.miwa.controller;

import com.tech.app.miwa.model.Transaction;
import com.tech.app.miwa.model.User;
import com.tech.app.miwa.model.Wallet;
import com.tech.app.miwa.repository.TransactionRepository;
import com.tech.app.miwa.repository.UserRepository;
import com.tech.app.miwa.repository.WalletRepository;
import com.tech.app.miwa.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WalletController {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Wallet> wallets = walletRepository.findByUser(user);

        model.addAttribute("username", username);
        model.addAttribute("wallets", wallets);

        return "dashboard";
    }

    @PostMapping("/wallet/topup")
    public String topUp(@RequestParam Long walletId, @RequestParam double amount) {
        walletService.topUp(walletId, amount);
        return "redirect:/dashboard";
    }

    @PostMapping("/wallet/transfer")
    public String transfer(@RequestParam Long fromWalletId, @RequestParam Long toWalletId, @RequestParam double amount) {
        walletService.transfer(fromWalletId, toWalletId, amount);
        return "redirect:/dashboard";
    }

    @GetMapping("/wallet/history/{walletId}")
    public String history(@PathVariable Long walletId, Model model) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow();
        List<Transaction> history = transactionRepository.findBySourceWalletOrTargetWallet(wallet, wallet);
        model.addAttribute("transactions", history);
        return "history";
    }

}
