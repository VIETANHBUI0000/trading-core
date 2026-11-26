# Trading Core

Spring Boot API demo cho hệ thống đặt lệnh chứng khoán (SQL Server + Redis + JWT).

## Cấu hình

1. Sao chép `src/main/resources/application-local.properties.example` thành `application-local.properties` (cùng thư mục).
2. Điền thông tin database và `jwt.secret` (tối thiểu 32 ký tự).

Hoặc đặt biến môi trường: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`.

## Chạy

```bash
./mvnw spring-boot:run
```

> Lưu ý: `AuthController` dùng mật khẩu cố định `123456` chỉ phục vụ mục đích demo.
