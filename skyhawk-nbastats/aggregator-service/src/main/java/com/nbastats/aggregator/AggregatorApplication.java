package com.nbastats.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AggregatorApplication.class, args);
	}

}
