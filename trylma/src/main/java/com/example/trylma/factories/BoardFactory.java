package com.example.trylma.factories;

import com.example.trylma.board.ChineseCheckersBoard;
import com.example.trylma.board.DefaultBoardWithPlacedPawns;
import com.example.trylma.game.GameType;

public class BoardFactory {
    public static ChineseCheckersBoard createBoard(String gameTypeString, int numberOfPlayers) {
        GameType gameType = GameType.fromString(gameTypeString);
        
        switch (gameType) {
            case DEFAULT -> {
                DefaultBoardWithPlacedPawns board = new DefaultBoardWithPlacedPawns(numberOfPlayers);
                board.setStartingAndTargetPositions(numberOfPlayers);
                board.placePawns();

                return board;
            }
        }

        return null;
    }
}
