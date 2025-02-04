package com.example.trylma.interfaces;

import java.util.ArrayList;

import com.example.trylma.board.Pawn;

public interface Player {
    String getUsername();
    ArrayList<Pawn> getPawns();
    void setPawns(ArrayList<Pawn> pawns);
    void setStartingPositions(ArrayList<Field> startingPositions);
    void setTargetPositions(ArrayList<Field> targetPositions);
    ArrayList<Field> getStartingPositions();
    ArrayList<Field> getTargetPositions();
    boolean isPlayerTurn();
    void setPlayerTurn(boolean playerTurn);
}
