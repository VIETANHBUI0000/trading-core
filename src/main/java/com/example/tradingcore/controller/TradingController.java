package com.example.tradingcore.controller;

import com.example.tradingcore.dto.PlaceOrderRequest;
import com.example.tradingcore.entity.StockOrder;
import com.example.tradingcore.service.TradingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class TradingController {

    private final TradingService tradingService;

    @PostMapping("/buy")
    public ResponseEntity<StockOrder> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        StockOrder createdOrder = tradingService.placeBuyOrder(request);
        return ResponseEntity.ok(createdOrder);
    }


    @GetMapping("/wallet/{accountNo}")
    public ResponseEntity<com.example.tradingcore.entity.Wallet> getBalance(@PathVariable String accountNo) {
        return ResponseEntity.ok(tradingService.getWalletBalance(accountNo));
    }

    @GetMapping("/history/{accountNo}")
    public ResponseEntity<java.util.List<StockOrder>> getHistory(@PathVariable String accountNo) {
        return ResponseEntity.ok(tradingService.getOrderHistory(accountNo));
    }
}