package com.example.tradingcore.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    // Khóa bí mật (Bắt buộc dài tối thiểu 32 ký tự cho chuẩn HMAC-SHA256)
    // Giá trị được đọc từ cấu hình jwt.secret (biến môi trường JWT_SECRET hoặc application-local.properties)
    private final SecretKey secretKey;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 1. Sinh JWT Token có hạn 24 giờ cho số tài khoản chứng khoán
    public String generateToken(String accountNo) {
        return Jwts.builder()
                .subject(accountNo)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24 giờ
                .signWith(secretKey)
                .compact();
    }

    // 2. Giải mã Token để lấy ra số tài khoản (Nếu token giả hoặc hết hạn sẽ tự quăng lỗi)
    public String extractAccountNo(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}