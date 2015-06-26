package com.king.minigame.backend.model.services;

import com.king.minigame.backend.model.exceptions.AuthenticationException;

/**
 * Created by enrique on 25/06/15.
 */
public interface GameService {

    /**
     * Creates a new session for a user.
     *
     * @param userId userId for which the session will be created.
     * @return Session key for the new active session.
     */
    String login(int userId);

    /**
     * Registers a user score for the specified level.
     * Only one request per level and user is allowed, if more that
     * one appear, only the highest one will be saved.
     *
     * @param sessionKey Session key that identifies the user.
     * @param levelId Id of the level for which the score will be saved.
     * @param score Score that will be registered in the level.
     */
    void registerScore(String sessionKey, int levelId, int score) throws AuthenticationException;

    /**
     * Gets the list of the highest scores for the specified level.
     *
     * @param levelId Id of the level we want to list the scores for.
     * @return User scores for the specified level, in descendant order.
     */
    String getHighScoresForLevel(int levelId);

    /**
     * Method to determine if a session has expired or is still active.
     *
     * @param sessionKey Session key of the session.
     * @return True if the session is still an active session, false otherwise.
     */
    boolean sessionKeyIsActive(String sessionKey);

    /**
     * Method to remove expired sessions from the list of active sessions.
     *
     */
    void clearInvalidSessions();
}
