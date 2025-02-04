package com.chinesecheckers.factories;

import com.chinesecheckers.board.ChineseCheckersBoard;
import com.chinesecheckers.board.DefaultBoardWithBotPlayer;
import com.chinesecheckers.board.DefaultBoardWithPlacedPawns;
import com.chinesecheckers.board.YinAndYangBoard;
import com.chinesecheckers.game.BotPlayer;
import com.chinesecheckers.game.GameType;
import com.chinesecheckers.server.GameServer;

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
