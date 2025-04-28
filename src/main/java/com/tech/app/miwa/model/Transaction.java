package com.tech.app.miwa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // "TOPUP", "TRANSFER"

    private Double amount;

    private String description;

    private LocalDateTime timestamp;

    @ManyToOne
    private Wallet sourceWallet;

    @ManyToOne
    private Wallet targetWallet;
}
