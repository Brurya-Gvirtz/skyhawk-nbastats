package com.skyhawk.statsaggregation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StatsAggregationApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatsAggregationApplication.class, args);
	}

}
