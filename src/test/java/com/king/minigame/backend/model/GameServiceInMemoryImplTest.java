package com.king.minigame.backend.model;

import com.king.minigame.backend.model.exceptions.AuthenticationException;
import com.king.minigame.backend.model.services.GameService;
import com.king.minigame.backend.model.services.impl.GameServiceInMemoryImpl;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by enrique on 25/06/15.
 */
public class GameServiceInMemoryImplTest {

    private static final String NON_EXISTENT_SESSION_KEY = "nonExistentSessionKey";

    private final GameService game = new GameServiceInMemoryImpl(600000);

    @Test
    public void login_createsNewActiveSessionForUser() {
        int userId = 1;
        String sessionKey = game.login(userId);
        assertTrue(game.sessionKeyIsActive(sessionKey));
    }

    @Test
    public void sessionKeyIsActive_withNoActiveSessions_isFalse() {
        assertFalse(game.sessionKeyIsActive(NON_EXISTENT_SESSION_KEY));
    }

    @Test
    public void sessionKeyIsNonActive_forExpiredSession_isFalse() {
        int userId = 1;
        int sessionTTL = -1;
        GameService gameWithTTLZero = new GameServiceInMemoryImpl(sessionTTL);

        String sessionKey = gameWithTTLZero.login(userId);
        assertFalse(gameWithTTLZero.sessionKeyIsActive(sessionKey));
    }

    @Test(expected = AuthenticationException.class)
    public void registerScore_forNonActiveSessionKey_throwsAuthenticationException() throws AuthenticationException {
        int levelId = 1;
        int score = 10;
        game.registerScore(NON_EXISTENT_SESSION_KEY, levelId, score);
    }

    @Test
    public void registerScore_forActiveSessionKeyAndNewLevel_createsLevelAndSavesScore() throws AuthenticationException {
        int userId = 1;
        int levelId = 2;
        int score = 30;
        String activeSessionKey = game.login(userId);

        game.registerScore(activeSessionKey, levelId, score);
        assertEquals(game.getHighScoresForLevel(levelId), userId + "=" + score);
    }

    @Test
    public void registerScore_forActiveSessionKeyAndExistingLevel_savesScore() throws AuthenticationException {
        int userId = 1;
        int levelId = 2;
        int originalScore = 30;
        int newHigherScore = 50;
        String activeSessionKey = game.login(userId);

        game.registerScore(activeSessionKey, levelId, originalScore);
        game.registerScore(activeSessionKey, levelId, newHigherScore);
        assertEquals(game.getHighScoresForLevel(levelId), userId + "=" + newHigherScore);
    }
}
