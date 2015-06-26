package com.king.minigame.backend.server;

import com.king.minigame.backend.server.exceptions.InvalidUriException;

import static com.king.minigame.backend.server.GameHttpParams.*;

/**
 * Created by enrique on 25/06/15.
 */
public enum GameHttpRequest {

    LOGIN_REQUEST("login", "/(\\d*)/login"),
    NEW_SCORE_REQUEST("score", "/(\\d*)/score\\?" + SESSION_KEY.getParameterName() +"=(.*)"),
    LIST_SCORE_REQUEST("highscorelist", "/(\\d*)/highscorelist");

    private final String endpoint;
    private final String urlPattern;

    private GameHttpRequest(String endpoint, String urlPattern) {
        this.endpoint = endpoint;
        this.urlPattern = urlPattern;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public static GameHttpRequest parseFromUri(String url) throws InvalidUriException {
        for (GameHttpRequest request : values()) {
            if (url.matches(request.getUrlPattern())) {
                return request;
            }
        }
        throw new InvalidUriException(url + " is not a valid url.");
    }

    public static boolean uriIsValidRequest(String url) {
        try {
            parseFromUri(url);
            return true;
        } catch (InvalidUriException e) {
            return false;
        }
    }
}
