package com.chinesecheckers.interfaces;

import com.chinesecheckers.board.Move;
import com.chinesecheckers.exceptions.InvalidMoveException;

public interface MoveParser {
    Move parseMove(String message) throws InvalidMoveException;
}
