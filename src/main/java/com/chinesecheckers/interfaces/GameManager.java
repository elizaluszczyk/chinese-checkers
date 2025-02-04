package com.chinesecheckers.interfaces;

import com.chinesecheckers.board.ChineseCheckersBoard;
import com.chinesecheckers.board.Move;

public interface GameManager {
    ChineseCheckersBoard getBoard();
    boolean isMoveValid(Move move, Player player);
    void applyMove(Move move);
    boolean isWinningMove(Player player);
    void applyMoveForBot(Move move);
}
