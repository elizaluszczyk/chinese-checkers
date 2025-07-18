package com.chinesecheckers.board;

import com.chinesecheckers.interfaces.Field;
import java.io.Serializable;

public class Pawn implements Serializable {
  private static final long serialVersionUID = 1L;
  private Field currentField;
  private final String playerId;

  public Pawn(String playerId, Field initialField, String color, int id) {
    this.currentField = initialField;
    this.playerId = playerId;
  }

  public Pawn(String playerId, Field initialField, int id) {
    this.currentField = initialField;
    this.playerId = playerId;
  }

  public Field getCurrentField() {
    return currentField;
  }

  public void setCurrentField(Field currentField) {
    this.currentField = currentField;
  }

  public String getPlayerId() {
    return playerId;
  }
}
