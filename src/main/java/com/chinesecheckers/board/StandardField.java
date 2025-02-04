package com.chinesecheckers.board;

import java.io.Serializable;

import com.chinesecheckers.interfaces.Field;

public class StandardField implements Field, Serializable {
    private static final long serialVersionUID = 1L;
    private final int x;
    private final int y;
    private boolean active;
    private boolean occupied;
    private Pawn pawn;

    public StandardField(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }
    @Override
    public int getY() {
        return y;
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
    
    @Override
    public boolean isOccupied() {
        return occupied;
    }
    
    @Override
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    @Override
    public Pawn getPawn() {
        return pawn;
    }

    @Override
    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }

    @Override
    public String toString() {
        if (!active) {
            return " ";
        } else if (!occupied){
            return "*";
        } else {
            return "@";
        }
    }
}

