package com.example.trylma.interfaces;

import com.example.trylma.board.Move;

public interface GameManager {
    Board getBoard();
    boolean isMoveValid(Move move, Player player);
    void applyMove(Move move);
    boolean isWinningMove(Move move, Player player);
}
