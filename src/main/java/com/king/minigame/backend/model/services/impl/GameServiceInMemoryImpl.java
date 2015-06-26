package com.king.minigame.backend.model.services.impl;

import com.king.minigame.backend.model.exceptions.AuthenticationException;

import com.king.minigame.backend.model.entities.Level;
import com.king.minigame.backend.model.entities.UserScore;
import com.king.minigame.backend.model.entities.Session;
import com.king.minigame.backend.model.services.GameService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by enrique on 25/06/15.
 */
public class GameServiceInMemoryImpl implements GameService {

    private final int sessionTTL;
    private final ConcurrentMap<Integer, Level> levels;
    private final ConcurrentMap<String, Session> activeSessions;

    public GameServiceInMemoryImpl(int sessionTTL) {
        this.sessionTTL = sessionTTL;
        this.levels = new ConcurrentHashMap<>();
        this.activeSessions = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized String login(int userId) {
        Session newSession = new Session(userId, System.currentTimeMillis(), sessionTTL);
        activeSessions.put(newSession.getSessionKey(), newSession);
        return newSession.getSessionKey();
    }

    @Override
    public synchronized void registerScore(String sessionKey, int levelId, int score) throws AuthenticationException {
        if (!sessionKeyIsActive(sessionKey)) {
            throw new AuthenticationException();
        }
        UserScore userScore = new UserScore(activeSessions.get(sessionKey).getUserId(), score);
        Level level = levels.get(levelId);
        if (level == null)  {
            level = new Level(levelId);
            levels.putIfAbsent(levelId, level);
        }
        level.addScore(userScore);
    }

    @Override
    public synchronized String getHighScoresForLevel(int levelId) {
        return levels.containsKey(levelId) ? levels.get(levelId).getHighScoresAsString() : "";
    }

    @Override
    public synchronized void clearInvalidSessions() {
        for (Session session : activeSessions.values()) {
            if (!session.isValid()) {
                activeSessions.remove(session.getSessionKey());
            }
        }
    }

    @Override
    public boolean sessionKeyIsActive(String sessionKey) {
        Session activeSession = activeSessions.get(sessionKey);
        return activeSession != null && activeSession.isValid();
    }
}
