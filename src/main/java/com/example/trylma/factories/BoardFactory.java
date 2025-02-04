package com.example.trylma.factories;

import com.example.trylma.board.ChineseCheckersBoard;
import com.example.trylma.board.DefaultBoardWithBotPlayer;
import com.example.trylma.board.DefaultBoardWithPlacedPawns;
import com.example.trylma.board.YinAndYangBoard;
import com.example.trylma.game.BotPlayer;
import com.example.trylma.game.GameType;
import com.example.trylma.server.GameServer;

public class BoardFactory {
    public static ChineseCheckersBoard createBoard(String gameTypeString, int numberOfPlayers) {
        GameType gameType = GameType.fromString(gameTypeString);
        
        switch (gameType) {
            case YINANDYANG -> {
                YinAndYangBoard board = new YinAndYangBoard(numberOfPlayers);
                board.setStartingAndTargetPositions(numberOfPlayers);
                board.placePawns();

                return board;
            }
            case DEFAULT -> {
                DefaultBoardWithPlacedPawns board = new DefaultBoardWithPlacedPawns(numberOfPlayers);
                board.setStartingAndTargetPositions(numberOfPlayers);
                board.placePawns();

                return board;
            }
            case DEFAULTWITHBOT -> {
                BotPlayer botPlayer = GameServer.getBotPlayer();

                DefaultBoardWithBotPlayer board = new DefaultBoardWithBotPlayer(numberOfPlayers, botPlayer);
                board.setStartingAndTargetPositions(numberOfPlayers);
                board.placePawns();

                return board;
            }
        }

        return null;
    }
}
