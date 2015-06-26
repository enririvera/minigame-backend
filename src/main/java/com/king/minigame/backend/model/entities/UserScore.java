package com.king.minigame.backend.model.entities;

/**
 * Created by enrique on 25/06/15.
 */
public class UserScore implements Comparable<UserScore>{

    private final int userId;
    private final int score;

    public UserScore(int userId, int score) {
        this.userId = userId;
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return userId + "=" + score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserScore userScore = (UserScore) o;

        if (score != userScore.score) return false;
        if (userId != userScore.userId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + score;
        return result;
    }

    @Override
    public int compareTo(UserScore other) {
        if (other == null) {
            return 1;
        }
        int compare = Integer.compare(other.score, this.score);
        if (compare == 0 && Integer.compare(other.userId, this.userId) != 0) {
            compare = 1;
        }
        return compare;
    }
}
