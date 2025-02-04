package com.example.trylma.packets;

import com.example.trylma.board.Move;
import com.example.trylma.server.ServerPacket;

public class MovePacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final Move move;

    public MovePacket(Move move) {
        this.move = move;
    }

    public Move getMove() {
        return move;
    }
}
