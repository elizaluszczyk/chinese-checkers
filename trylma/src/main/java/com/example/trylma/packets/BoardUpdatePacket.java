package com.example.trylma.packets;

import com.example.trylma.interfaces.Board;
import com.example.trylma.server.ServerPacket;

public class BoardUpdatePacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final Board board;

    public BoardUpdatePacket(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }
}
