package com.example.trylma.factories;

import com.example.trylma.board.DefaultBoardWithPlacedPawns;
import com.example.trylma.interfaces.Board;

public class BoardFactory {
    public static Board createBoard(String gameType, int numberOfPlayers) {
        switch (gameType) {
            case "default" -> {
            switch (numberOfPlayers) {
                case 2 -> {
                    return new DefaultBoardWithPlacedPawns(2);
                }
                case 3 -> {
                    return new DefaultBoardWithPlacedPawns(3);
                }
                case 4 -> {
                    return new DefaultBoardWithPlacedPawns(4);
                }
                case 6 -> {
                    return new DefaultBoardWithPlacedPawns(6);
                }
                default -> throw new IllegalArgumentException("Unsupported number of players: " + numberOfPlayers);
            }
            }
            default -> throw new IllegalArgumentException("Unknown game type: " + gameType);
        }
    }
}
