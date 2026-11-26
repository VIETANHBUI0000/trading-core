package com.example.tradingcore.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PlaceOrderRequest(
        @NotBlank(message = "Số tài khoản không được trống")
        String accountNo,

        @NotBlank(message = "Mã chứng khoán không được trống")
        String symbol,

        @NotNull @Positive(message = "Giá đặt phải lớn hơn 0")
        BigDecimal price,

        @NotNull @Min(value = 100, message = "Khối lượng tối thiểu là 100 cổ phiếu")
        Integer quantity
) {}