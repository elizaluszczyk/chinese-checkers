package com.chinesecheckers.packets;

import com.chinesecheckers.board.Move;
import com.chinesecheckers.server.ServerPacket;

public class InvalidMovePacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final Move invalidMove;

    public InvalidMovePacket(Move invalidMove) {
        this.invalidMove = invalidMove;
    }

    public Move getInvalidMove() {
        return invalidMove;
    }
}
