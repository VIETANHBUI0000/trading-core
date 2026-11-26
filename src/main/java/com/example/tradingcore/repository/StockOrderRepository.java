package com.example.tradingcore.repository;

import com.example.tradingcore.entity.StockOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockOrderRepository extends JpaRepository<StockOrder, Long> {
    // Tương đương: .Where(x => x.AccountNo == accountNo).OrderByDescending(x => x.CreatedAt) bên LINQ C#
    List<StockOrder> findByAccountNoOrderByCreatedAtDesc(String accountNo);
}