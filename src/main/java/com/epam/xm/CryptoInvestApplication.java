package com.epam.xm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CryptoInvestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoInvestApplication.class, args);
	}

}
