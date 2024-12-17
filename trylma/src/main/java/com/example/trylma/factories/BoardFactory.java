package com.example.trylma.factories;

import com.example.trylma.game.DefaultChineseCheckersBoard;
import com.example.trylma.interfaces.Board;;

public class BoardFactory {
    public static Board createBoard(String gameType, int numberOfPlayers) {
        switch (gameType) {
            case "default" -> {
            switch (numberOfPlayers) {
                case 2 -> {
                    return new DefaultChineseCheckersBoard(2);
                }
                case 4 -> {
                    return new DefaultChineseCheckersBoard(4);
                }
                case 6 -> {
                    return new DefaultChineseCheckersBoard(6);
                }
                default -> throw new IllegalArgumentException("Unsupported number of players: " + numberOfPlayers);
            }
            }
            default -> throw new IllegalArgumentException("Unknown game type: " + gameType);
        }
    }
}
