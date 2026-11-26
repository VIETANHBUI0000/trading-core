package com.example.tradingcore.service;

import com.example.tradingcore.dto.PlaceOrderRequest;
import com.example.tradingcore.entity.StockOrder;
import com.example.tradingcore.entity.Wallet;
import com.example.tradingcore.repository.StockOrderRepository;
import com.example.tradingcore.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TradingService {

    private final WalletRepository walletRepository;
    private final StockOrderRepository orderRepository;
    private final StockPriceCacheService priceCacheService; // 1. Khai báo thêm biến này

    // @Transactional tương đương using var transaction = dbContext.Database.BeginTransaction()
    // Nếu có bất kỳ RuntimeException nào ném ra, toàn bộ lệnh trừ tiền sẽ tự động Rollback!
    @Transactional
    public StockOrder placeBuyOrder(PlaceOrderRequest request) {
        // 1. Chặn spam/double-click bằng Redis Distributed Lock
        priceCacheService.checkDuplicateOrder(request.accountNo(), request.symbol());

        // 2. Kiểm tra biên độ giá Trần/Sàn trên Redis
        priceCacheService.validateOrderPrice(request.symbol(), request.price());
        // 2. Kiểm tra giá Trần/Sàn từ Redis ngay lập tức trước khi chạm vào SQL Server!
        priceCacheService.validateOrderPrice(request.symbol(), request.price());
        // 1. Tìm ví theo số tài khoản
        Wallet wallet = walletRepository.findByAccountNo(request.accountNo())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        // 2. Tính tổng giá trị lệnh = Giá * Khối lượng
        BigDecimal totalCost = request.price().multiply(BigDecimal.valueOf(request.quantity()));

        // 3. Kiểm tra số dư (Trong BigDecimal: a.compareTo(b) < 0 nghĩa là a < b)
        if (wallet.getBalance().compareTo(totalCost) < 0) {
            throw new RuntimeException("Số dư không đủ để đặt lệnh!");
        }

        // 4. Phong tỏa / Trừ tiền trong ví
        wallet.setBalance(wallet.getBalance().subtract(totalCost));
        walletRepository.save(wallet);

        // 5. Tạo lệnh ở trạng thái PENDING
        StockOrder newOrder = StockOrder.builder()
                .accountNo(request.accountNo())
                .symbol(request.symbol().toUpperCase())
                .price(request.price())
                .quantity(request.quantity())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        return orderRepository.save(newOrder);
    }

    public Wallet getWalletBalance(String accountNo) {
        return walletRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví của tài khoản: " + accountNo));
    }

    public java.util.List<StockOrder> getOrderHistory(String accountNo) {
        return orderRepository.findByAccountNoOrderByCreatedAtDesc(accountNo);
    }
}