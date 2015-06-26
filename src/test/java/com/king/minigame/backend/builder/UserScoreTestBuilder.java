package com.king.minigame.backend.builder;

import com.king.minigame.backend.model.entities.UserScore;

/**
 * Created by enrique on 25/06/15.
 */
public class UserScoreTestBuilder {

    private int userId;
    private int score;

    public static UserScoreTestBuilder aUserScore() {
        return new UserScoreTestBuilder();
    }

    public UserScoreTestBuilder withUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public UserScoreTestBuilder withScore(int score) {
        this.score = score;
        return this;
    }

    public UserScore build() {
        return new UserScore(userId, score);
    }
}
