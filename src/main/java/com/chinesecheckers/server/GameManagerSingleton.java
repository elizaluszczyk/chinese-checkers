package com.chinesecheckers.server;

import com.chinesecheckers.game.StandardGameManager;
import com.chinesecheckers.interfaces.GameManager;

public class GameManagerSingleton {
    private static GameManager instance;

    private GameManagerSingleton() {}

    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new StandardGameManager("default", 2);
        }
        return instance;
    }

    public static synchronized void setInstance(GameManager newGameManager) {
        instance = newGameManager;
    }
}
