package com.king.minigame.backend.server;

import com.king.minigame.backend.model.services.GameService;
import com.king.minigame.backend.model.services.impl.GameServiceInMemoryImpl;
import com.king.minigame.backend.server.scheduler.SessionCleanerScheduler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by enrique on 25/06/15.
 */
public class GameHttpServer {

    private static final int PORT = 8081;
    private static final int sessionTTL = 600000;

    public static void main(String[] args) throws Exception {

        try {
            System.out.println("\nStarting HTTP Server.");
            String hostName = "localhost";
            try {
                hostName = InetAddress.getLocalHost().getCanonicalHostName();
            } catch (UnknownHostException ex) {
                System.err.println("Unknown Host: " + ex);
            }

            GameService game = new GameServiceInMemoryImpl(sessionTTL);
            SessionCleanerScheduler sessionCleaner = new SessionCleanerScheduler(game, sessionTTL);
            sessionCleaner.start();

            HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            HttpContext httpContext = httpServer.createContext("/", new GameHttpHandler(game));
            httpContext.getFilters().add(new GameHttpFilter());
            ExecutorService executorService = Executors.newCachedThreadPool();
            httpServer.setExecutor(executorService);
            httpServer.start();
            System.out.println("The HTTP Server was started in http://" + hostName + ":" + PORT + "/");
            System.out.println("The HTTP Server is up and running!\n");
        } catch (Exception e) {
            System.err.println("Error starting the HTTP server.");
            System.err.println(e.getMessage());
        }
    }
}
