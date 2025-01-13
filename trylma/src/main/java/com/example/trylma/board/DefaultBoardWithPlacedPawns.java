package com.example.trylma.board;

import com.example.trylma.game.GamePlayer;
import com.example.trylma.server.ClientHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultBoardWithPlacedPawns extends ChineseCheckersBoard {
    private final int numberOfPlayers;
//    private ArrayList<ArrayList<Field>> startingPositions;
//ArrayList<GamePlayer> listOfPlayers = ClientHandler.getAllPlayers();
    GamePlayer gamePlayer1 = new GamePlayer("adam");
    GamePlayer gamePlayer2 = new GamePlayer("karol");
ArrayList<GamePlayer> listOfPlayers = new ArrayList<>(Arrays.asList(gamePlayer1, gamePlayer2));



    public DefaultBoardWithPlacedPawns(int numberOfPlayers) {
        super(5);
        this.numberOfPlayers = numberOfPlayers;
//        this.startingPositions = new ArrayList<>();
        setStartingAndTargetPositions(numberOfPlayers);
        placePawns();
    }


    public void placePawns(){
        for(GamePlayer player : listOfPlayers){
            for (int i = 0; i < player.getStartingPositions().size(); i++){
                Pawn pawn = new Pawn(player.getUsername(), player.getStartingPositions().get(i), i);
                player.getPawns().add(pawn);
                player.getStartingPositions().get(i).setPawn(pawn);
                player.getStartingPositions().get(i).setOccupied(true);

            }
        }
    }

    public void setStartingAndTargetPositions(int numberOfPlayers){
        switch (numberOfPlayers) {
            case 2:
                listOfPlayers.get(0).setStartingPositions(getArmsOfStar().get(0));
                listOfPlayers.get(0).setTargetPositions(getArmsOfStar().get(3));
                listOfPlayers.get(1).setStartingPositions(getArmsOfStar().get(3));
                listOfPlayers.get(1).setTargetPositions(getArmsOfStar().get(0));
                break;
            case 3:
                listOfPlayers.get(0).setStartingPositions(getArmsOfStar().get(0));
                listOfPlayers.get(0).setTargetPositions(getArmsOfStar().get(3));
                listOfPlayers.get(1).setStartingPositions(getArmsOfStar().get(2));
                listOfPlayers.get(1).setTargetPositions(getArmsOfStar().get(5));
                listOfPlayers.get(2).setStartingPositions(getArmsOfStar().get(4));
                listOfPlayers.get(2).setTargetPositions(getArmsOfStar().get(1));
                break;
            case 4:
                listOfPlayers.get(0).setStartingPositions(getArmsOfStar().get(1));
                listOfPlayers.get(0).setTargetPositions(getArmsOfStar().get(4));
                listOfPlayers.get(1).setStartingPositions(getArmsOfStar().get(2));
                listOfPlayers.get(1).setTargetPositions(getArmsOfStar().get(5));
                listOfPlayers.get(2).setStartingPositions(getArmsOfStar().get(4));
                listOfPlayers.get(2).setTargetPositions(getArmsOfStar().get(1));
                listOfPlayers.get(3).setStartingPositions(getArmsOfStar().get(5));
                listOfPlayers.get(3).setTargetPositions(getArmsOfStar().get(2));
                break;
            case 6:
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
                listOfPlayers.get(5).setStartingPositions(getArmsOfStar().get(5));
                listOfPlayers.get(5).setTargetPositions(getArmsOfStar().get(2));
                break;
            default:
                throw new IllegalArgumentException("Invalid number of players: " + numberOfPlayers);


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
