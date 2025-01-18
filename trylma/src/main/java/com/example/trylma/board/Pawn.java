package com.example.trylma.board;

import java.io.Serializable;

import com.example.trylma.interfaces.Field;

public class Pawn implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Field currentField;

    public Pawn(String playerId, Field initialField, String color, int id) {
        this.currentField = initialField;
    }    
    
    public Pawn(String playerId, Field initialField, int id) {
        this.currentField = initialField;
    }

    public Field getCurrentField() {
        return currentField;
    }
}
