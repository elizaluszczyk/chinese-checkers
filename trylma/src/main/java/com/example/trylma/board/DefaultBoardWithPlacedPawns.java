package com.example.trylma.board;

import java.util.ArrayList;

import com.example.trylma.interfaces.Field;
import com.example.trylma.interfaces.Player;
import com.example.trylma.server.GameServer;

public class DefaultBoardWithPlacedPawns extends ChineseCheckersBoard {
    protected final ArrayList<Player> listOfPlayers;

    public DefaultBoardWithPlacedPawns(int numberOfPlayers) {
        super(5);
        this.listOfPlayers = new ArrayList<>(GameServer.getAllPlayers());
    }
    
    public void placePawns() {
        for (Player player : listOfPlayers) {
            if (!player.getPawns().isEmpty()) {
                continue; 
            }
            
            for (int i = 0; i < player.getStartingPositions().size(); i++) {
                Pawn pawn = new Pawn(player.getUsername(), (Field) player.getStartingPositions().get(i), i);
                player.getPawns().add(pawn);
                player.getStartingPositions().get(i).setPawn(pawn);
                player.getStartingPositions().get(i).setOccupied(true);
            }
        }
    }
    
    public void setStartingAndTargetPositions(int numberOfPlayers) {
        if (listOfPlayers.size() < numberOfPlayers) return;
        
        switch (numberOfPlayers) {
            case 2 -> {
                listOfPlayers.get(0).setStartingPositions(getArmsOfStar().get(0));
                listOfPlayers.get(0).setTargetPositions(getArmsOfStar().get(3));
                listOfPlayers.get(1).setStartingPositions(getArmsOfStar().get(3));
                listOfPlayers.get(1).setTargetPositions(getArmsOfStar().get(0));
            }
            case 3 -> {
                listOfPlayers.get(0).setStartingPositions(getArmsOfStar().get(0));
                listOfPlayers.get(0).setTargetPositions(getArmsOfStar().get(3));
                listOfPlayers.get(1).setStartingPositions(getArmsOfStar().get(2));
                listOfPlayers.get(1).setTargetPositions(getArmsOfStar().get(5));
                listOfPlayers.get(2).setStartingPositions(getArmsOfStar().get(4));
                listOfPlayers.get(2).setTargetPositions(getArmsOfStar().get(1));
            }
            case 4 -> {
                listOfPlayers.get(0).setStartingPositions(getArmsOfStar().get(1));
                listOfPlayers.get(0).setTargetPositions(getArmsOfStar().get(4));
                listOfPlayers.get(1).setStartingPositions(getArmsOfStar().get(2));
                listOfPlayers.get(1).setTargetPositions(getArmsOfStar().get(5));
                listOfPlayers.get(2).setStartingPositions(getArmsOfStar().get(4));
                listOfPlayers.get(2).setTargetPositions(getArmsOfStar().get(1));
                listOfPlayers.get(3).setStartingPositions(getArmsOfStar().get(5));
                listOfPlayers.get(3).setTargetPositions(getArmsOfStar().get(2));
            }
            case 6 -> {
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
            }
            default -> throw new IllegalArgumentException("Invalid number of players: " + numberOfPlayers);
            }
        }
    
    @Override
    public ArrayList<Player> getListOfPlayers() {
        return listOfPlayers;
    }
}
