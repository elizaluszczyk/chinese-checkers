package com.example.trylma.interfaces;

import com.example.trylma.board.Pawn;

public interface Field {
    int getX();
    int getY();
    boolean isActive();
    void setActive(boolean active);
    boolean isOccupied();
    void setOccupied(boolean occupied);
    void setPawn(Pawn pawn);
    Pawn getPawn();
}

