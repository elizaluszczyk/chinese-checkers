package com.example.trylma.interfaces;
import com.example.trylma.board.Position;

public interface Move {

    Position getStartPosition();
    Position getEndPosition();
    void executeMove(Board board);
    boolean isValidMove(Board board);
}
