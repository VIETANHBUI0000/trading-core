package com.example.tradingcore.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class StockPriceCacheService {

    // StringRedisTemplate dùng để đọc/ghi dữ liệu dạng Key-Value lên Redis
    private final StringRedisTemplate redisTemplate;

    // @PostConstruct tự động chạy 1 lần ngay khi ứng dụng khởi động xong
    // Giả lập nạp sẵn Giá Trần và Giá Sàn của mã FPT, VCB lên Redis đầu phiên giao dịch
    @PostConstruct
    public void initMarketPriceCache() {
        redisTemplate.opsForHash().put("PRICE_LIMIT:FPT", "FLOOR", "120000");
        redisTemplate.opsForHash().put("PRICE_LIMIT:FPT", "CEILING", "140000");

        redisTemplate.opsForHash().put("PRICE_LIMIT:VCB", "FLOOR", "85000");
        redisTemplate.opsForHash().put("PRICE_LIMIT:VCB", "CEILING", "98000");
    }

    public void validateOrderPrice(String symbol, BigDecimal orderPrice) {
        String key = "PRICE_LIMIT:" + symbol.toUpperCase();

        Object floorObj = redisTemplate.opsForHash().get(key, "FLOOR");
        Object ceilingObj = redisTemplate.opsForHash().get(key, "CEILING");

        if (floorObj == null || ceilingObj == null) {
            throw new RuntimeException("Mã chứng khoán " + symbol + " không tồn tại trên sàn!");
        }

        BigDecimal floorPrice = new BigDecimal(floorObj.toString());
        BigDecimal ceilingPrice = new BigDecimal(ceilingObj.toString());

        if (orderPrice.compareTo(floorPrice) < 0 || orderPrice.compareTo(ceilingPrice) > 0) {
            throw new RuntimeException(
                    String.format("Giá đặt (%s) vượt ngoài biên độ Trần/Sàn [%s - %s]!",
                            orderPrice, floorPrice, ceilingPrice)
            );
        }
    }

    public void checkDuplicateOrder(String accountNo, String symbol) {
        String lockKey = "ORDER_LOCK:" + accountNo + ":" + symbol.toUpperCase();

        // setIfAbsent tương đương lệnh SETNX trong Redis:
        // Nếu chưa có key -> Tạo key có hiệu lực 3 giây và trả về true
        // Nếu đã có key (khách vừa bấm cách đây chưa tới 3 giây) -> Trả về false
        Boolean isFirstClick = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "LOCKED", Duration.ofSeconds(3));

        if (Boolean.FALSE.equals(isFirstClick)) {
            throw new RuntimeException("Thao tác quá nhanh! Vui lòng đợi 3 giây trước khi đặt tiếp mã " + symbol);
        }
    }
}