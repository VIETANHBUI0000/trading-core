package com.example.tradingcore.controller;

import com.example.tradingcore.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String accountNo,
                                        @RequestParam String password) {
        if ("123456".equals(password)) {
            String token = jwtService.generateToken(accountNo);
            return ResponseEntity.ok(Map.of(
                    "accountNo", accountNo,
                    "accessToken", token
            ));
        }
        throw new RuntimeException("Sai số tài khoản hoặc mật khẩu!");
    }
}