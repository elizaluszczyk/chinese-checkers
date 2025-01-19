package com.example.trylma.interfaces;

import com.example.trylma.board.ChineseCheckersBoard;
import com.example.trylma.board.Move;

public interface GameManager {
    ChineseCheckersBoard getBoard();
    boolean isMoveValid(Move move, Player player);
    void applyMove(Move move);
    boolean isWinningMove(Player player);
}
