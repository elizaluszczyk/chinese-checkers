package com.chinesecheckers.interfaces;

import com.chinesecheckers.board.Pawn;
import java.util.ArrayList;

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
