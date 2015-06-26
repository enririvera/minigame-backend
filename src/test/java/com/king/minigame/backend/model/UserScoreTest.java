package com.king.minigame.backend.model;

import com.king.minigame.backend.model.entities.UserScore;
import org.junit.Test;

import static com.king.minigame.backend.builder.UserScoreTestBuilder.aUserScore;
import static org.junit.Assert.assertEquals;

/**
 * Created by enrique on 25/06/15.
 */
public class UserScoreTest {

    @Test
    public void compareTo_withNull() {
        assertEquals(aUserScore().build().compareTo(null), 1);
    }

    @Test
    public void compareTo_withLowerScoreByAnotherUser() {
        UserScore higherUserScore = aUserScore()
                .withUserId(1)
                .withScore(30)
                .build();
        UserScore lowerUserScoreByDifferentUser = aUserScore()
                .withUserId(2)
                .withScore(10)
                .build();

        assertEquals(higherUserScore.compareTo(lowerUserScoreByDifferentUser), -1);
    }

    @Test
    public void compareTo_withHigherScoreByAnotherUser() {
        UserScore higherUserScore = aUserScore()
                .withUserId(1)
                .withScore(30)
                .build();
        UserScore lowerUserScoreByDifferentUser = aUserScore()
                .withUserId(2)
                .withScore(10)
                .build();

        assertEquals(lowerUserScoreByDifferentUser.compareTo(higherUserScore), 1);
    }

    @Test
    public void compareTo_withSameScoreBySameUser() {
        UserScore aUserScore = aUserScore()
                .withUserId(1)
                .withScore(30)
                .build();
        UserScore anotherScoreWithSameValueAndUser = aUserScore()
                .withUserId(1)
                .withScore(30)
                .build();

        assertEquals(aUserScore.compareTo(anotherScoreWithSameValueAndUser), 0);
        assertEquals(anotherScoreWithSameValueAndUser.compareTo(aUserScore), 0);
    }

    @Test
    public void compareTo_withSameScoreByDifferentUser() {
        UserScore aUserScore = aUserScore()
                .withUserId(1)
                .withScore(30)
                .build();
        UserScore sameScoreByDifferentUser = aUserScore()
                .withUserId(2)
                .withScore(30)
                .build();

        assertEquals(aUserScore.compareTo(sameScoreByDifferentUser), 1);
    }

    @Test
    public void toString_returnsUserScoreWithRightFormat() {
        UserScore aUserScore = aUserScore()
                .withUserId(1)
                .withScore(30)
                .build();

        assertEquals(aUserScore.toString(), aUserScore.getUserId() + "=" + aUserScore.getScore());
    }
}
