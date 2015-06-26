package com.king.minigame.backend.server;

/**
 * Created by enrique on 25/06/15.
 */
public enum GameHttpParams {

    SCORE("score"),
    SESSION_KEY("sessionkey"),
    LEVEL_ID("levelid"),
    USER_ID("userid");

    private final String parameterName;

    private GameHttpParams(String parameterName) { this.parameterName = parameterName; }

    public String getParameterName() { return parameterName; }
}
