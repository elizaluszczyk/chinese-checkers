package com.chinesecheckers.board;

import com.chinesecheckers.interfaces.Field;
import com.chinesecheckers.interfaces.Player;
import com.chinesecheckers.server.GameServer;
import java.util.ArrayList;

public class DefaultBoardWithPlacedPawns extends ChineseCheckersBoard {
  protected final ArrayList<Player> listOfPlayers;

  public DefaultBoardWithPlacedPawns(int numberOfPlayers) {
    super(5);
    this.listOfPlayers = new ArrayList<>(GameServer.getAllPlayers());
  }

  public void placePawns() {
    for (Player player : listOfPlayers) {
      if (player.getPawns().isEmpty()) {
        placePlayerPawns(player);
      }
    }
  }

  private void placePlayerPawns(Player player) {
    ArrayList<Field> startingPositions = player.getStartingPositions();
    for (int i = 0; i < startingPositions.size(); i++) {
      Field field = startingPositions.get(i);
      Pawn pawn = new Pawn(player.getUsername(), field, i);
      player.getPawns().add(pawn);
      field.setPawn(pawn);
      field.setOccupied(true);
    }
  }

  public void setStartingAndTargetPositions(int numberOfPlayers) {
    if (listOfPlayers.size() < numberOfPlayers) return;

    switch (numberOfPlayers) {
      case 2 -> {
        setPositions(0, 0, 3);
        setPositions(1, 3, 0);
      }
      case 3 -> {
        setPositions(0, 0, 3);
        setPositions(1, 2, 5);
        setPositions(2, 4, 1);
      }
      case 4 -> {
        setPositions(0, 1, 4);
        setPositions(1, 2, 5);
        setPositions(2, 4, 1);
        setPositions(3, 5, 2);
      }
      case 6 -> {
        for (int i = 0; i < 6; i++) {
          setPositions(i, i, (i + 3) % 6);
        }
      }
      default ->
          throw new IllegalArgumentException("Invalid number of players: " + numberOfPlayers);
    }
  }

  protected void setPositions(int playerIndex, int startArmIndex, int targetArmIndex) {
    if (playerIndex < listOfPlayers.size()) {
      Player player = listOfPlayers.get(playerIndex);
      player.setStartingPositions(getArmsOfStar().get(startArmIndex));
      player.setTargetPositions(getArmsOfStar().get(targetArmIndex));
    }
  }
}
