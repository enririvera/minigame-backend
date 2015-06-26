package com.king.minigame.backend.server;

import com.king.minigame.backend.server.exceptions.InvalidUriException;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by enrique on 25/06/15.
 */
public class GameHttpFilter extends Filter {

    @Override
    public synchronized void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        try {
            if (GameHttpRequest.uriIsValidRequest(httpExchange.getRequestURI().toString())) {
                URI uri = httpExchange.getRequestURI();
                Map<String, String> parameters = new HashMap<String, String>();
                parseGetParameters(uri, parameters);
                parsePostParameters(httpExchange, parameters);
                parseUrlEncodedParameters(uri, parameters);
                httpExchange.setAttribute("parameters", parameters);
                chain.doFilter(httpExchange);
            } else {
                setHttpErrorResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST, "Request not valid.");
            }
        } catch (InvalidUriException e) {
            setHttpErrorResponse(httpExchange, HttpURLConnection.HTTP_NOT_FOUND, "Url not found in the server.");
        }
    }

    @Override
    public String description() {
        return "Filter for the mini game backend server.";
    }

    private void setHttpErrorResponse(HttpExchange httpExchange, int errorCode, String errorMessage) throws IOException {
        httpExchange.sendResponseHeaders(errorCode, errorMessage.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(errorMessage.getBytes());
        os.close();
    }

    private void parseGetParameters(URI requestedUri, Map<String, String> parameters) throws UnsupportedEncodingException {
        String query = requestedUri.getRawQuery();
        if (query != null) {
            parseQuery(query, parameters);
        }
    }

    private void parsePostParameters(HttpExchange exchange, Map<String, String> parameters) throws IOException {
        if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(),"utf-8");
            BufferedReader br = new BufferedReader(isr);
            String score = br.readLine();
            parameters.put(GameHttpParams.SCORE.getParameterName(), score);
        }
    }

    private void parseUrlEncodedParameters(URI uri, Map<String, String> parameters) throws InvalidUriException {
        GameHttpRequest request = GameHttpRequest.parseFromUri(uri.toString());
        String[] tokens = uri.toString().split("[/?=]");
        switch (request) {
            case LOGIN_REQUEST:
                parameters.put(GameHttpParams.USER_ID.getParameterName(), tokens[1]);
                break;
            case NEW_SCORE_REQUEST:
            case LIST_SCORE_REQUEST:
                parameters.put(GameHttpParams.LEVEL_ID.getParameterName(), tokens[1]);
                break;
            default:
                throw new InvalidUriException("Uri not recognized by the server filter.");
        }
    }

    private void parseQuery(String query, Map<String, String> parameters) throws UnsupportedEncodingException {
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            parameters.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
    }
}