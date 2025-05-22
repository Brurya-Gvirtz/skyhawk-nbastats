package com.nbastats.flusher;

import com.nbastats.flusher.service.FlusherService;

public class FlusherApplication {
    public static void main(String[] args) {
        System.out.println("Starting End Game Flusher Service...");
        FlusherService flusherService = new FlusherService();
        flusherService.start();
    }
}
