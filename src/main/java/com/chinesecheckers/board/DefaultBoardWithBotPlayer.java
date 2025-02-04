package com.example.trylma.board;

import com.example.trylma.game.BotPlayer;

public class DefaultBoardWithBotPlayer extends DefaultBoardWithPlacedPawns {
    private final BotPlayer botPlayer;
    
    public DefaultBoardWithBotPlayer(int numberOfPlayers, BotPlayer botPlayer) {
        super(numberOfPlayers);
        this.botPlayer = botPlayer;
        this.listOfPlayers.add(botPlayer);
    }
    
    @Override
    public void setStartingAndTargetPositions(int numberOfPlayers) {
        if (listOfPlayers.size() < numberOfPlayers) {
            return;
        }

        if (listOfPlayers.contains(botPlayer)) {
            switch (numberOfPlayers) {
                case 1 -> {
                    listOfPlayers.get(0).setStartingPositions(getArmsOfStar().get(0));
                    listOfPlayers.get(0).setTargetPositions(getArmsOfStar().get(3));
                    botPlayer.setStartingPositions(getArmsOfStar().get(3));
                    botPlayer.setTargetPositions(getArmsOfStar().get(0));
                }
                case 2 -> {
                    listOfPlayers.get(0).setStartingPositions(getArmsOfStar().get(0));
                    listOfPlayers.get(0).setTargetPositions(getArmsOfStar().get(3));
                    listOfPlayers.get(1).setStartingPositions(getArmsOfStar().get(2));
                    listOfPlayers.get(1).setTargetPositions(getArmsOfStar().get(5));
                    botPlayer.setStartingPositions(getArmsOfStar().get(4));
                    botPlayer.setTargetPositions(getArmsOfStar().get(1));
                }
                case 3 -> {
                    listOfPlayers.get(0).setStartingPositions(getArmsOfStar().get(0));
                    listOfPlayers.get(0).setTargetPositions(getArmsOfStar().get(3));
                    listOfPlayers.get(1).setStartingPositions(getArmsOfStar().get(2));
                    listOfPlayers.get(1).setTargetPositions(getArmsOfStar().get(5));
                    listOfPlayers.get(2).setStartingPositions(getArmsOfStar().get(4));
                    listOfPlayers.get(2).setTargetPositions(getArmsOfStar().get(1));
                    botPlayer.setStartingPositions(getArmsOfStar().get(1));
                    botPlayer.setTargetPositions(getArmsOfStar().get(4));
                }
                case 5 -> {
                    listOfPlayers.get(0).setStartingPositions(getArmsOfStar().get(0));
                    listOfPlayers.get(0).setTargetPositions(getArmsOfStar().get(3));
                    listOfPlayers.get(1).setStartingPositions(getArmsOfStar().get(1));
                    listOfPlayers.get(1).setTargetPositions(getArmsOfStar().get(4));
                    listOfPlayers.get(2).setStartingPositions(getArmsOfStar().get(2));
                    listOfPlayers.get(2).setTargetPositions(getArmsOfStar().get(5));
                    listOfPlayers.get(3).setStartingPositions(getArmsOfStar().get(3));
                    listOfPlayers.get(3).setTargetPositions(getArmsOfStar().get(0));
                    listOfPlayers.get(4).setStartingPositions(getArmsOfStar().get(4));
                    listOfPlayers.get(4).setTargetPositions(getArmsOfStar().get(1));
                    botPlayer.setStartingPositions(getArmsOfStar().get(5));
                    botPlayer.setTargetPositions(getArmsOfStar().get(2));
                }  
                default -> throw new IllegalArgumentException("Invalid number of players for bot: " + numberOfPlayers);
            }
        }
    }

    public BotPlayer getBotPlayer() {
        return botPlayer;
    }
}
