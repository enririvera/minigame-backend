package com.king.minigame.backend.model;

import com.king.minigame.backend.model.entities.Session;
import org.junit.Test;

import static com.king.minigame.backend.builder.SessionTestBuilder.aSession;

import static org.junit.Assert.*;

/**
 * Created by enrique on 25/06/15.
 */
public class SessionTest {

    @Test
    public void isValid_whenExpired_returnsFalse() {
        int timeToLive = 300000;
        Session aExpiredSession = aSession()
                .withTimeToLive(timeToLive)
                .withCreatedAt(System.currentTimeMillis() - timeToLive * 2)
                .build();
        assertFalse(aExpiredSession.isValid());
    }

    @Test
    public void isValid_whenNotExpired_returnsTrue() {
        int timeToLive = 200000;
        Session aValidSession = aSession()
                .withTimeToLive(timeToLive)
                .withCreatedAt(System.currentTimeMillis() - timeToLive / 2)
                .build();
        assertTrue(aValidSession.isValid());
    }
}
