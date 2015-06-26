package com.king.minigame.backend.server;

import com.king.minigame.backend.model.exceptions.AuthenticationException;
import com.king.minigame.backend.model.services.GameService;
import com.king.minigame.backend.server.exceptions.InvalidUriException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import static com.king.minigame.backend.server.GameHttpParams.*;

/**
 * Created by enrique on 25/06/15.
 */
public class GameHttpHandler implements HttpHandler {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TEXT = "text/plain";

    private final GameService game;

    public GameHttpHandler(GameService  game) { this.game = game; }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String responseBody = "";
        int responseCode = HttpURLConnection.HTTP_OK;
        Map<String, String> parameters = (Map) httpExchange.getAttribute("parameters");
        try {
            GameHttpRequest request = GameHttpRequest.parseFromUri(httpExchange.getRequestURI().toString());
            switch (request) {
                case LOGIN_REQUEST:
                    int userId = Integer.parseInt(parameters.get(USER_ID.getParameterName()));
                    responseBody = game.login(userId);
                    break;
                case NEW_SCORE_REQUEST:
                    String sessionKey = parameters.get(SESSION_KEY.getParameterName());
                    int score = Integer.parseInt(parameters.get(SCORE.getParameterName()));
                    int levelIdForNewScore = Integer.parseInt(parameters.get(LEVEL_ID.getParameterName()));
                    game.registerScore(sessionKey, levelIdForNewScore, score);
                    break;
                case LIST_SCORE_REQUEST:
                    int levelIdForList = Integer.parseInt(parameters.get(LEVEL_ID.getParameterName()));
                    responseBody = game.getHighScoresForLevel(levelIdForList);
                    break;
                default:
                    throw new InvalidUriException("Invalid request.");
            }
        } catch (NumberFormatException e) {
            responseBody = "Invalid number format.";
            responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
        } catch (InvalidUriException e) {
            responseBody = e.getMessage();
            responseCode = HttpURLConnection.HTTP_NOT_FOUND;
        } catch (AuthenticationException e) {
            responseBody = "Authentication is needed to access this url.";
            responseCode = HttpURLConnection.HTTP_UNAUTHORIZED;
        }catch (Exception e) {
            responseBody = "Something went wrong when handling the request.";
            responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
        }

        httpExchange.getResponseHeaders().add(CONTENT_TYPE, CONTENT_TEXT);
        httpExchange.sendResponseHeaders(responseCode, responseBody.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(responseBody.getBytes());
        os.close();
    }
}
