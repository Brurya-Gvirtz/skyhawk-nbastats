package com.nbastats.flusher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlusherApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlusherApplication.class, args);
    }
}
