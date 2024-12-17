package com.example.trylma.interfaces;

import com.example.trylma.board.Move;
import com.example.trylma.exceptions.InvalidMoveException;

public interface MoveParser {
    Move parseMove(String message) throws InvalidMoveException;
}
