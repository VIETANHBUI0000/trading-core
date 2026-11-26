package com.example.tradingcore.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNo; // Số tiểu khoản chứng khoán

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal balance; // Số dư khả dụng

    @Version // Cơ chế Optimistic Locking chống ghi đè khi 2 luồng trừ tiền cùng lúc!
    private Long version;
}