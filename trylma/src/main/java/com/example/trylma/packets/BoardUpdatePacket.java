package com.example.trylma.packets;

import com.example.trylma.ServerPacket;
import com.example.trylma.interfaces.Board;

public class BoardUpdatePacket implements ServerPacket {
    private final Board board;

    public BoardUpdatePacket(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }
}
