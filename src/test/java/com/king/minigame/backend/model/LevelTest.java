package com.king.minigame.backend.model;

import com.king.minigame.backend.model.entities.Level;
import com.king.minigame.backend.model.entities.UserScore;
import org.junit.Test;

import static com.king.minigame.backend.builder.LevelTestBuilder.aLevel;
import static com.king.minigame.backend.builder.UserScoreTestBuilder.aUserScore;

import static org.junit.Assert.*;

/**
 * Created by enrique on 25/06/15.
 */
public class LevelTest {

    @Test
    public void addScore_toLevelWithZeroScores_scoreIsFound() {
        Level aLevel = aLevel().build();
        UserScore aUserScore = aUserScore().withUserId(1).withScore(30).build();

        aLevel.addScore(aUserScore);
        UserScore scoreFound = aLevel.findScoreForUser(aUserScore.getUserId());

        assertEquals(scoreFound, aUserScore);
        assertEquals(aLevel.getNumberOfScores(), 1);
    }

    @Test
    public void addScore_toLevelWithLowerScoreForUser_newScoreIsFound() {
        int userId = 1;
        UserScore lowerUserScore = aUserScore()
                .withUserId(userId)
                .withScore(30)
                .build();
        UserScore higherUserScore = aUserScore()
                .withUserId(userId)
                .withScore(35)
                .build();
        Level aLevel = aLevel().build();

        aLevel.addScore(lowerUserScore);
        aLevel.addScore(higherUserScore);
        UserScore userScoreFound = aLevel.findScoreForUser(userId);

        assertEquals(userScoreFound, higherUserScore);
        assertEquals(aLevel.getNumberOfScores(), 1);
    }

    @Test
    public void addScore_toLevelWithHigherScoreForUser_previousScoreIsFound() {
        int userId = 1;
        UserScore lowerUserScore = aUserScore()
                .withUserId(userId)
                .withScore(30)
                .build();
        UserScore higherUserScore = aUserScore()
                .withUserId(userId)
                .withScore(35)
                .build();
        Level aLevel = aLevel().build();

        aLevel.addScore(higherUserScore);
        aLevel.addScore(lowerUserScore);
        UserScore userScoreFound = aLevel.findScoreForUser(userId);

        assertEquals(userScoreFound, higherUserScore);
        assertEquals(aLevel.getNumberOfScores(), 1);
    }

    @Test
    public void addScore_toLevelFullWithHigherScores_newScoreIsNotSaved() {
        Level aLevel = aLevel().build();
        UserScore minUserScoreForLevel = fillLevelWithDifferentUserScoresAndReturnMinUserScore(aLevel);
        UserScore newUserScore = aUserScore()
                .withUserId(-1)
                .withScore(minUserScoreForLevel.getScore() - 10)
                .build();

        aLevel.addScore(newUserScore);
        UserScore userScoreFound = aLevel.findScoreForUser(newUserScore.getUserId());

        assertNull(userScoreFound);
        assertEquals(aLevel.getNumberOfScores(), Level.MAX_NUMBER_SCORES);
    }

    @Test
    public void addScore_toLevelFullWithLoweScores_newScoreReplacesMinScore() {
        Level aLevel = aLevel().build();
        UserScore minUserScoreForLevel = fillLevelWithDifferentUserScoresAndReturnMinUserScore(aLevel);
        UserScore higherNewScoreForLevel = aUserScore()
                .withUserId(-1)
                .withScore(minUserScoreForLevel.getScore() + 1)
                .build();

        aLevel.addScore(higherNewScoreForLevel);
        UserScore newScoreFound = aLevel.findScoreForUser(higherNewScoreForLevel.getUserId());
        UserScore previousMinScoreFound = aLevel.findScoreForUser(minUserScoreForLevel.getUserId());

        assertEquals(newScoreFound, higherNewScoreForLevel);
        assertNull(previousMinScoreFound);
        assertEquals(aLevel.getNumberOfScores(), Level.MAX_NUMBER_SCORES);
    }

    @Test
    public void addScore_toLevelFullWithMinScoreEqualToNewScore_newScoreIsNotSaved() {
        Level aLevel = aLevel().build();
        UserScore minUserScoreForLevel = fillLevelWithDifferentUserScoresAndReturnMinUserScore(aLevel);
        UserScore newUserScore = aUserScore()
                .withUserId(-1)
                .withScore(minUserScoreForLevel.getScore())
                .build();

        aLevel.addScore(newUserScore);
        UserScore newScoreFound = aLevel.findScoreForUser(newUserScore.getUserId());
        UserScore originalMinScoreFound = aLevel.findScoreForUser(minUserScoreForLevel.getUserId());

        assertNull(newScoreFound);
        assertEquals(originalMinScoreFound, minUserScoreForLevel);
        assertEquals(aLevel.getNumberOfScores(), Level.MAX_NUMBER_SCORES);
    }

    @Test
    public void getHighScores_forLevelWithZeroScores_isEmpty() {
        Level aLevel = aLevel().build();
        assertEquals(aLevel.getHighScoresAsString(), "");
    }

    @Test
    public void getHighScores_forLevelWithSomeScores_formattedScoresOrdered() {
        Level aLevel = aLevel().build();
        Level anotherLevelWithScoresAddedInDifferentOrder = aLevel().build();

        UserScore lowerUserScore = aUserScore()
                .withUserId(2)
                .withScore(5)
                .build();
        UserScore higherUserScore = aUserScore()
                .withUserId(1)
                .withScore(10)
                .build();

        aLevel.addScore(lowerUserScore);
        aLevel.addScore(higherUserScore);
        anotherLevelWithScoresAddedInDifferentOrder.addScore(higherUserScore);
        anotherLevelWithScoresAddedInDifferentOrder.addScore(lowerUserScore);

        assertEquals(aLevel.getHighScoresAsString(), "1=10,2=5");
        assertEquals(anotherLevelWithScoresAddedInDifferentOrder.getHighScoresAsString(), "1=10,2=5");
    }

    private UserScore fillLevelWithDifferentUserScoresAndReturnMinUserScore(Level level) {
        UserScore minUserScore = null;
        for (int i = 0; i < Level.MAX_NUMBER_SCORES; i++) {
            UserScore score = aUserScore().withUserId(i).withScore(i).build();
            if (i == 0) {
                minUserScore = score;
            }
            level.addScore(score);
        }
        assertEquals(level.getNumberOfScores(), Level.MAX_NUMBER_SCORES);
        return minUserScore;
    }
}
