package com.king.minigame.backend.model.entities;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by enrique on 25/06/15.
 */
public class Level {

    public static final int MAX_NUMBER_SCORES = 15;

    private final int id;
    private final ConcurrentSkipListSet<UserScore> highScores = new ConcurrentSkipListSet<UserScore>();

    public Level(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public synchronized void addScore(UserScore newUserScore) {
        if (highScores.size() < MAX_NUMBER_SCORES || newUserScore.getScore() > highScores.last().getScore()) {
            UserScore previousScoreForUser = findScoreForUser(newUserScore.getUserId());
            if (previousScoreForUser != null){
                if (previousScoreForUser.getScore() < newUserScore.getScore()) {
                    highScores.remove(previousScoreForUser);
                    highScores.add(newUserScore);
                }
            } else {
                highScores.add(newUserScore);
                if (highScores.size() > MAX_NUMBER_SCORES) {
                    highScores.pollLast();
                }
            }
        }
    }

    public synchronized UserScore findScoreForUser(int userId) {
        for (UserScore score : highScores) {
            if (score.getUserId() == userId) {
                return score;
            }
        }
        return null;
    }

    public int getNumberOfScores() {
        return highScores.size();
    }

    public String getHighScoresAsString() {
        return highScores.toString().replace("[", "").replace("]", "").replace(", ", ",");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Level level = (Level) o;

        return id == level.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
