package com.example.trylma.board;

import java.io.Serializable;

import com.example.trylma.interfaces.Field;

public class Pawn implements Serializable {
    private static final long serialVersionUID = 1L;
    //private final String playerId;
    private final Field currentField;
    //private  String color;
    //private final int id;

    public Pawn(String playerId, Field initialField, String color, int id) {
        //this.playerId = playerId;
        this.currentField = initialField;
        //this.color = color;
        //this.id = id;
//            if (initialField != null) {
//                initialField.setPiece(this);
//            }
    }    public Pawn(String playerId, Field initialField, int id) {
        //this.playerId = playerId;
        this.currentField = initialField;
        //this.id = id;
    }
    public Field getCurrentField() {
        return currentField;
    }

    @Override
    public String toString() {
        return "O";
    }
}
