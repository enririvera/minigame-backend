package com.king.minigame.backend.model.entities;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by enrique on 25/06/15.
 */
public class Session implements Serializable {

    private final int userId;
    private final String sessionKey;
    private final long createdAt;
    private final int timeToLive;

    public Session(int userId, long createdAt, int timeToLive) {
        this.userId = userId;
        this.sessionKey = UUID.randomUUID().toString().replace("-", "");
        this.createdAt = createdAt;
        this.timeToLive = timeToLive;
    }

    public int getUserId() {
        return userId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public boolean isValid() {
        return (System.currentTimeMillis() - createdAt <= timeToLive);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (createdAt != session.createdAt) return false;
        if (timeToLive != session.timeToLive) return false;
        if (userId != session.userId) return false;
        if (sessionKey != null ? !sessionKey.equals(session.sessionKey) : session.sessionKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (sessionKey != null ? sessionKey.hashCode() : 0);
        result = 31 * result + (int) (createdAt ^ (createdAt >>> 32));
        result = 31 * result + timeToLive;
        return result;
    }
}
