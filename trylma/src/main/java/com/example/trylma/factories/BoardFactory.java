package com.example.trylma.factories;

import com.example.trylma.board.DefaultBoardWithPlacedPawns;
import com.example.trylma.game.GameType;
import com.example.trylma.interfaces.Board;

public class BoardFactory {
    public static Board createBoard(String gameTypeString, int numberOfPlayers) {
        GameType gameType = GameType.fromString(gameTypeString);
        
        return switch (gameType) {
            case DEFAULT -> new DefaultBoardWithPlacedPawns(numberOfPlayers);
        };
    }
}
