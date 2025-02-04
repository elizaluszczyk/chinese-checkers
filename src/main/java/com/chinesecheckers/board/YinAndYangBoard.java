package com.example.trylma.board;

import java.util.Random;

public class YinAndYangBoard extends DefaultBoardWithPlacedPawns {

    public YinAndYangBoard(int numberOfPlayers) {
        super(numberOfPlayers);
    }

    @Override
    public void setStartingAndTargetPositions(int numberOfPlayers) {
        if (listOfPlayers.size() < numberOfPlayers) return;

        switch (numberOfPlayers) {
            case 2 -> {
                Random rand = new Random();

                int randomStarArm1 = rand.nextInt(6);
                int randomStarArm2;

                do {
                    randomStarArm2 = rand.nextInt(6);
                } while (randomStarArm2 == randomStarArm1);

                listOfPlayers.get(0).setStartingPositions(getArmsOfStar().get(randomStarArm1));
                listOfPlayers.get(0).setTargetPositions(getArmsOfStar().get(randomStarArm2));
                listOfPlayers.get(1).setStartingPositions(getArmsOfStar().get(randomStarArm2));
                listOfPlayers.get(1).setTargetPositions(getArmsOfStar().get(randomStarArm1));
            }
            default -> throw new IllegalArgumentException("Invalid number of players: " + numberOfPlayers);
        }
    }
}
