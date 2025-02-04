package com.chinesecheckers.interfaces;

import com.chinesecheckers.board.Pawn;

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

