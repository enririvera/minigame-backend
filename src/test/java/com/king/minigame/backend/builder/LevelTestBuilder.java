package com.king.minigame.backend.builder;

import com.king.minigame.backend.model.entities.Level;

/**
 * Created by enrique on 25/06/15.
 */
public class LevelTestBuilder {

    private int id;

    public static LevelTestBuilder aLevel() {
        return new LevelTestBuilder();
    }

    public LevelTestBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public Level build() {
        return new Level(id);
    }
}
