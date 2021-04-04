package com.deutsche.tradestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TradestoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradestoreApplication.class, args);
	}


}
