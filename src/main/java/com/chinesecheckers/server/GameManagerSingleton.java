package com.chinesecheckers.server;

import com.chinesecheckers.game.StandardGameManager;
import com.chinesecheckers.interfaces.GameManager;

public class GameManagerSingleton {
    private static GameManager instance;

    private static final String DEFAULT_GAME_TYPE = "default";
    private static final int DEFAULT_PLAYER_COUNT = 2;

    private GameManagerSingleton() {}

    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new StandardGameManager(DEFAULT_GAME_TYPE, DEFAULT_PLAYER_COUNT);
        }
        return instance;
    }

    public static synchronized void setInstance(GameManager newGameManager) {
        instance = newGameManager;
    }
}
