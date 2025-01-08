package com.example.trylma.board;

import java.io.Serializable;
public class Pawn implements Serializable {
    private final String playerId;
    private Field currentField;
    private final String color;
    private final int id;

    public Pawn(String playerId, Field initialField, String color, int id) {
        this.playerId = playerId;
        this.currentField = initialField;
        this.color = color;
        this.id = id;
//            if (initialField != null) {
//                initialField.setPiece(this);
//            }
    }
    public Field getCurrentField() {
        return currentField;
    }
}
