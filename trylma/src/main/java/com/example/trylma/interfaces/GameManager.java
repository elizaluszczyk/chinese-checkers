package com.example.trylma.interfaces;

import com.example.trylma.board.Move;

public interface GameManager {
    Board getBoard();
    boolean isMoveValid(Move move);
    void applyMove(Move move);
}
