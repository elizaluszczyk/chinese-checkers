package com.example.chinese_checkers.interfaces;

import com.example.chinese_checkers.board.Position;

public interface Move {

    Position getStartPosition();
    Position getEndPosition();
    void executeMove(Board board);
    boolean isValidMove(Board board);
}
