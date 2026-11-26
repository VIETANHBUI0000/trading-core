package com.example.tradingcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootApplication
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class TradingcoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingcoreApplication.class, args);
	}

}