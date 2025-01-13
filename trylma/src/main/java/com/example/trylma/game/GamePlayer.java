package com.example.trylma.game;

import com.example.trylma.board.Field;
import com.example.trylma.board.Pawn;
import com.example.trylma.interfaces.Player;


import java.util.ArrayList;
import java.util.List;

public class GamePlayer implements Player {
    private final String username;
    private ArrayList<Field> startingPositions;
    private ArrayList<Field> targetPositions;

    private ArrayList<Pawn> pawns = new ArrayList<>();

    public GamePlayer(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public ArrayList<Pawn> getPawns() {
        return pawns;
    }

    public void setPawns(ArrayList<Pawn> pawns) {
        this.pawns = pawns;
    }

    public void setStartingPositions(ArrayList<Field> startingPositions) {
        this.startingPositions = startingPositions;
    }

    public void setTargetPositions(ArrayList<Field> targetPositions) {
        this.targetPositions = targetPositions;
    }

    public ArrayList<Field> getStartingPositions() {
        return startingPositions;
    }

    public ArrayList<Field> getTargetPositions() {
        return targetPositions;
    }
}
