package com.example.trylma.board;

import java.util.ArrayList;
import java.util.List;

public class DefaultBoardWithPlacedPawns extends ChineseCheckersBoard {
//    private  DefaultChineseCheckersBoard board;
    private final int numberOfPlayers;
    private ArrayList<ArrayList<Field>> startingPositions;

    public DefaultBoardWithPlacedPawns(int numberOfPlayers) {
        super(5);
        this.numberOfPlayers = numberOfPlayers;
        this.startingPositions = new ArrayList<>();
//        placePawns;
    }



    public void selectStaringPositions(int numberOfPlayers){
        ArrayList<ArrayList<Field>> startingPositions = getStartingPositions();
        switch (numberOfPlayers) {
            case 2:
                this.startingPositions.add(startingPositions.get(0));
                this.startingPositions.add(startingPositions.get(3));
                break;
            case 3:
                this.startingPositions.add(startingPositions.get(0));
                this.startingPositions.add(startingPositions.get(2));
                this.startingPositions.add(startingPositions.get(4));
            case 4:
                this.startingPositions.add(startingPositions.get(0));
                this.startingPositions.add(startingPositions.get(1));
                this.startingPositions.add(startingPositions.get(3));
                this.startingPositions.add(startingPositions.get(4));
            case 6:
                this.startingPositions = startingPositions;



        }
    }




    public void printBoard() {
        for (List<Field> row : getBoard()) {
            for (Field field : row) {
                System.out.print(field.isActive() ? field.toString() : "{ }");
            }
            System.out.println();
        }
    }



}
