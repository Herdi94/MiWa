package com.tech.app.miwa.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;

    private String description;

    @JsonFormat(pattern = "d/M/yyyy, h:mm:ss a",
                locale = "en_US")
    private LocalDateTime timestamp;

    @ManyToOne
    private Wallet sourceWallet;

    @ManyToOne
    private Wallet targetWallet;
}
