package com.king.minigame.backend.server;

import com.king.minigame.backend.server.exceptions.InvalidUriException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by enrique on 25/06/15.
 */
public class GameHttpRequestTest {

    @Test
    public void parseFromUri_forValidLoginUri_isMappedToLoginRequest() throws InvalidUriException {
        String validLoginUri = "/31829/login";
        GameHttpRequest request = GameHttpRequest.parseFromUri(validLoginUri);
        assertEquals(request, GameHttpRequest.LOGIN_REQUEST);
    }

    @Test
    public void parseFromUri_forValidNewScoreUri_isMappedToNewScoreRequest() throws InvalidUriException {
        String validNewScoreUri = "/38392/score?sessionkey=asdbv2319fajbk";
        GameHttpRequest request = GameHttpRequest.parseFromUri(validNewScoreUri);
        assertEquals(request, GameHttpRequest.NEW_SCORE_REQUEST);
    }

    @Test
    public void parseFromUri_forValidListScoresUri_isMappedToListScoresRequest() throws InvalidUriException {
        String validListScoreUri = "/38392/highscorelist";
        GameHttpRequest request = GameHttpRequest.parseFromUri(validListScoreUri);
        assertEquals(request, GameHttpRequest.LIST_SCORE_REQUEST);
    }

    @Test(expected = InvalidUriException.class)
    public void parseFromUri_forLoginRequestWithInvalidUserId_throwsInvalidUriException() throws InvalidUriException {
        String invalidLoginUri = "/asd231/login";
        GameHttpRequest.parseFromUri(invalidLoginUri);
    }

    @Test(expected = InvalidUriException.class)
    public void parseFromUri_forNewScoreRequestWithNoSessionKey_throwsInvalidUriException() throws InvalidUriException {
        String invalidNewScoreUri = "/38392/score";
        GameHttpRequest.parseFromUri(invalidNewScoreUri);
    }

    @Test(expected = InvalidUriException.class)
    public void parseFromUri_forNewScoreRequestWithInvalidUserId_throwsInvalidUriException() throws InvalidUriException {
        String invalidNewScoreUri = "/3839asda2/score?sessionkey=asdbv2319fajbk";
        GameHttpRequest.parseFromUri(invalidNewScoreUri);
    }

    @Test(expected = InvalidUriException.class)
    public void parseFromUri_forListScoreRequestWithInvalidUserId_throwsInvalidUriException() throws InvalidUriException {
        String invalidListScoreUri = "/383da9dasd2/highscorelist";
        GameHttpRequest.parseFromUri(invalidListScoreUri);
    }

    @Test
    public void uriIsValidRequest_forValidUris_isTrue() {
        assertTrue(GameHttpRequest.uriIsValidRequest("/31829/login"));
        assertTrue(GameHttpRequest.uriIsValidRequest("/38392/score?sessionkey=asdbv2319fajbk"));
        assertTrue(GameHttpRequest.uriIsValidRequest("/38392/highscorelist"));
    }

    @Test
    public void uriIsValidRequest_forInvalidUris_isFalse() {
        assertFalse(GameHttpRequest.uriIsValidRequest("/asd231/login"));
        assertFalse(GameHttpRequest.uriIsValidRequest("/38392/score"));
        assertFalse(GameHttpRequest.uriIsValidRequest("/3839asda2/score?sessionkey=asdbv2319fajbk"));
        assertFalse(GameHttpRequest.uriIsValidRequest("/383da9dasd2/highscorelist"));
        assertFalse(GameHttpRequest.uriIsValidRequest("/inventedEndPoint"));
    }
}
