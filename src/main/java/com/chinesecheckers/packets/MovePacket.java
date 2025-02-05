package com.chinesecheckers.packets;

import com.chinesecheckers.board.Move;
import com.chinesecheckers.server.ServerPacket;

public class MovePacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final Move move;

    public MovePacket(Move move) {
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public PacketType getType() {
        return PacketType.MOVE;
    }
}
