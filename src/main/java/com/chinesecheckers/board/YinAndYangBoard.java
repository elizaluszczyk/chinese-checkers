package com.chinesecheckers.board;

import java.util.Random;

public class YinAndYangBoard extends DefaultBoardWithPlacedPawns {
    private final Random random = new Random();

    public YinAndYangBoard(int numberOfPlayers) {
        super(numberOfPlayers);
    }

    @Override
    public void setStartingAndTargetPositions(int numberOfPlayers) {
        if (listOfPlayers.size() < numberOfPlayers) return;

        if (numberOfPlayers == 2) {
            setRandomOppositePositions();
        } else {
            throw new IllegalArgumentException("Invalid number of players: " + numberOfPlayers);
        }
    }
    
    private void setRandomOppositePositions() {
        int arm1 = random.nextInt(6);
        int arm2;
        do {
            arm2 = random.nextInt(6);
        } while (arm2 == arm1);
        
        setPositions(0, arm1, arm2);
        setPositions(1, arm2, arm1);
    }
}
