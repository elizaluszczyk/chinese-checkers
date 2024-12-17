package com.example.trylma.game;

import com.example.trylma.interfaces.Player;

public class GamePlayer implements Player {
    private final String username;

    public GamePlayer(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
