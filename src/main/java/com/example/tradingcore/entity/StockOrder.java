package com.example.tradingcore.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_orders")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class StockOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNo;
    private String symbol;     // Mã CK: FPT, VCB, HPG...
    private BigDecimal price;  // Giá đặt
    private Integer quantity;  // Khối lượng
    private String status;     // PENDING, MATCHED, REJECTED
    private LocalDateTime createdAt;
}