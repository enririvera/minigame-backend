package com.king.minigame.backend.builder;

import com.king.minigame.backend.model.entities.Session;

/**
 * Created by enrique on 25/06/15.
 */
public class SessionTestBuilder {

    private int userId;
    private long createdAt;
    private int timeToLive;

    public static SessionTestBuilder aSession() {
        return new SessionTestBuilder();
    }

    public SessionTestBuilder withUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public SessionTestBuilder withCreatedAt(long createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public SessionTestBuilder withTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }

    public Session build() {
        return new Session(userId, createdAt, timeToLive);
    }
}
