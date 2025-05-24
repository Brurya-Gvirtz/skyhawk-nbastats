package com.nbastats.flusher.service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * Dummy implementation to simulate end-of-game detection.
 * Calls the callback after a fixed delay.
 */
public class EndDetectionService {

    private final Consumer<String> onEndGameCallback;
    private final Timer timer = new Timer();

    // For demo, flush after 1 minute for a test gameId "game-123"
    private static final long INACTIVITY_TIMEOUT_MS = 60_000;

    public EndDetectionService(Consumer<String> onEndGameCallback) {
        this.onEndGameCallback = onEndGameCallback;
    }

    public void start() {
        System.out.println("Starting end detection service...");

        // Simulate detection with timer
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Simulate game ended for "game-123"
                onEndGameCallback.accept("game-123");
            }
        }, INACTIVITY_TIMEOUT_MS);
    }

    public void stop() {
        timer.cancel();
    }
}

