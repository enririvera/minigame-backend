package com.king.minigame.backend.server.scheduler;

import com.king.minigame.backend.model.services.GameService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by enrique on 21/06/15.
 */
public class SessionCleanerScheduler implements Runnable {

    private final GameService gameService;
    private final int executionRate;

    public SessionCleanerScheduler(GameService gameService, int executionRate) {
        this.gameService = gameService;
        this.executionRate = executionRate;
    }

    @Override
    public void run() {
        gameService.clearInvalidSessions();
    }

    public void start() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, executionRate, executionRate, TimeUnit.MILLISECONDS);
    }
}
