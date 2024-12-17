package com.example.trylma.interfaces;

import com.example.trylma.board.Pawn;

public interface Field {
    boolean isInBoard();
    void setPawn(Pawn pawn);
    Pawn getPawn();
    void setIsInBoard(boolean isInBoard);
}
