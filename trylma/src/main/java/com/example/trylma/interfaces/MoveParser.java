package com.example.trylma.interfaces;

import com.example.trylma.exceptions.InvalidMoveException;
import com.example.trylma.game.Move;

public interface MoveParser {
    Move parseMove(String message) throws InvalidMoveException;
}
