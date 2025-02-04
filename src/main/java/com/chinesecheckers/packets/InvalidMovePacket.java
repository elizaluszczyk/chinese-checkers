package com.example.trylma.packets;

import com.example.trylma.board.Move;
import com.example.trylma.server.ServerPacket;

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
