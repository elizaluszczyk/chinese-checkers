package com.example.trylma.game;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.trylma.board.Pawn;
import com.example.trylma.interfaces.Field;
import com.example.trylma.interfaces.Player;

public class GamePlayer implements Player, Serializable {
    private static final long serialVersionUID = 1L;
    private final String username;
    private ArrayList<Field> startingPositions = new ArrayList<>();
    private ArrayList<Field> targetPositions = new ArrayList<>();
    private ArrayList<Pawn> pawns = new ArrayList<>();

    public GamePlayer(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public ArrayList<Pawn> getPawns() {
        return pawns;
    }

    @Override
    public void setPawns(ArrayList<Pawn> pawns) {
        this.pawns = pawns;
    }

    @Override
    public void setStartingPositions(ArrayList<Field> startingPositions) {
        this.startingPositions = startingPositions;
    }

    @Override
    public void setTargetPositions(ArrayList<Field> targetPositions) {
        this.targetPositions = targetPositions;
    }

    @Override
    public ArrayList<Field> getStartingPositions() {
        return startingPositions;
    }

    @Override
    public ArrayList<Field> getTargetPositions() {
        return targetPositions;
    }
}
