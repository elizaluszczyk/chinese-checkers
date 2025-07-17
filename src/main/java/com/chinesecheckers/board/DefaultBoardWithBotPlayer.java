package com.chinesecheckers.board;

import com.chinesecheckers.game.BotPlayer;

public class DefaultBoardWithBotPlayer extends DefaultBoardWithPlacedPawns {
    private final BotPlayer botPlayer;
    
    public DefaultBoardWithBotPlayer(int numberOfPlayers, BotPlayer botPlayer) {
        super(numberOfPlayers);
        this.botPlayer = botPlayer;
        this.listOfPlayers.add(botPlayer);
    }
    
    @Override
    public void setStartingAndTargetPositions(int numberOfPlayers) {
        if (listOfPlayers.size() < numberOfPlayers || !listOfPlayers.contains(botPlayer)) {
            return;
        }

        switch (numberOfPlayers) {
            case 1 -> {
                setPositions(0, 0, 3);
                setBotPositions(3, 0);
            }
            case 2 -> {
                setPositions(0, 0, 3);
                setPositions(1, 2, 5);
                setBotPositions(4, 1);
            }
            case 3 -> {
                setPositions(0, 0, 3);
                setPositions(1, 2, 5);
                setPositions(2, 4, 1);
                setBotPositions(1, 4);
            }
            case 5 -> {
                for (int i = 0; i < 5; i++) {
                    setPositions(i, i, (i + 3) % 6);
                }
                setBotPositions(5, 2);
            }
            default -> throw new IllegalArgumentException("Invalid number of players for bot: " + numberOfPlayers);
        }
    }

    protected void setBotPositions(int startArmIndex, int targetArmIndex) {
        botPlayer.setStartingPositions(getArmsOfStar().get(startArmIndex));
        botPlayer.setTargetPositions(getArmsOfStar().get(targetArmIndex));
    }

    public BotPlayer getBotPlayer() {
        return botPlayer;
    }
}
