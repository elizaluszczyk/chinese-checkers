package com.example.trylma.board;

import java.util.ArrayList;
import java.util.List;

import com.example.trylma.interfaces.Field;
import com.example.trylma.interfaces.Player;
import com.example.trylma.server.GameServer;

public class DefaultBoardWithPlacedPawns extends ChineseCheckersBoard {
    // private final int numberOfPlayers;
    // private ArrayList<ArrayList<Field>> startingPositions;
    // ArrayList<GamePlayer> listOfPlayers = ClientHandler.getAllPlayers();
    // GamePlayer gamePlayer1 = new GamePlayer("adam");
    // GamePlayer gamePlayer2 = new GamePlayer("karol");
    // ArrayList<GamePlayer> listOfPlayers = new ArrayList<>(Arrays.asList(gamePlayer1, gamePlayer2));

    ArrayList<Player> listOfPlayers = new ArrayList<>(GameServer.getAllPlayers());

    public DefaultBoardWithPlacedPawns(int numberOfPlayers) {
        super(5);
        // this.numberOfPlayers = numberOfPlayers;
        // this.startingPositions = new ArrayList<>();
        setStartingAndTargetPositions(numberOfPlayers);
        placePawns();
    }

    @Override
    public void placePawns() {
        for (Player player : listOfPlayers) {
            for (int i = 0; i < player.getStartingPositions().size(); i++) {
                Pawn pawn = new Pawn(player.getUsername(), (Field) player.getStartingPositions().get(i), i);
                player.getPawns().add(pawn);
                player.getStartingPositions().get(i).setPawn(pawn);
                player.getStartingPositions().get(i).setOccupied(true);

            }
        }
    }

    public void setStartingAndTargetPositions(int numberOfPlayers) {
        if (listOfPlayers.size() < numberOfPlayers) {
            return;
        }

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

    @Override
    public void printBoard() {
        placePawns();
        for (List<Field> row : getBoard()) {
            for (Field field : row) {
                if (field.isActive()) {
                     System.out.print(field.toString());
                } else {
                    System.out.print("{ }");
                }
            }
            System.out.println();
        }
    }

}
