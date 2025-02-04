package com.example.trylma.server;

import com.example.trylma.game.StandardGameManager;
import com.example.trylma.interfaces.GameManager;

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
