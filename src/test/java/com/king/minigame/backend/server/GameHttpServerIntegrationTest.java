package com.king.minigame.backend.server;

import com.king.minigame.backend.model.services.impl.GameServiceInMemoryImpl;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * Created by enrique on 25/06/15.
 */
public class GameHttpServerIntegrationTest {

    private static final int PORT = 9090;
    private static final int sessionTTL = 600000;
    private HttpServer httpServer;
    private ExecutorService httpThreadPool;

    @Before
    public void setUp() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        HttpContext httpContext = httpServer.createContext("/", new GameHttpHandler(new GameServiceInMemoryImpl(sessionTTL)));
        httpContext.getFilters().add(new GameHttpFilter());
        httpThreadPool = Executors.newCachedThreadPool();
        httpServer.setExecutor(httpThreadPool);
        httpServer.start();
    }

    @After
    public void tearDown() {
        this.httpServer.stop(1);
        this.httpThreadPool.shutdownNow();
    }

    @Test
    public void loginAndPostScoreToLevel_verifyNewScoreIsListed() throws IOException {
        int userId = 1;
        int levelId = 12;
        int score = 30;
        String sessionKey = loginToServer(userId);
        postScoreToLevel(sessionKey, levelId, score);
        String highScoresForLevel = getScoresForLevel(levelId);
        assertEquals(highScoresForLevel, userId + "=" + score);
    }

    @Test
    public void loginAndPostTwoScoresForTheSameUser_verifyOnlyHigherIsListed() throws IOException {
        int userId = 1;
        int levelId = 12;
        int lowScore = 10;
        int highScore = 30;
        String sessionKey = loginToServer(userId);
        postScoreToLevel(sessionKey, levelId, lowScore);
        postScoreToLevel(sessionKey, levelId, highScore);
        String highScoresForLevel = getScoresForLevel(levelId);
        assertEquals(highScoresForLevel, userId + "=" + highScore);
    }

    @Test
    public void postScoreWithInvalidSessionKey_returnsHttpUnauthorized() throws IOException {
        int levelId = 12;
        int score = 10;
        int responseCode = postScoreToLevel("InvalidSessionKey", levelId, score);
        assertEquals(responseCode, HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    private String loginToServer(int userId) throws IOException {
        return sendGetRequestForUrl("http://localhost:" + PORT + "/" + userId + "/login");
    }

    private String getScoresForLevel(int levelId) throws IOException {
        return sendGetRequestForUrl("http://localhost:" + PORT + "/" + levelId + "/highscorelist");
    }

    private int postScoreToLevel(String sessionKey, int levelId, int score) throws IOException {
        URL url = new URL("http://localhost:" + PORT + "/" + levelId + "/score?sessionkey=" + sessionKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        conn.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(Integer.toString(score));
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        String response = null;
        if (responseCode != HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
            response = br.readLine();
        }
        conn.disconnect();
        return responseCode;
    }

    private String sendGetRequestForUrl(String urlAsString) throws IOException {
        URL url = new URL(urlAsString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = in.readLine();
        in.close();
        conn.disconnect();
        return response;
    }
}
