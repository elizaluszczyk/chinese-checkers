package com.example.trylma.packets;

import java.util.ArrayList;

import com.example.trylma.interfaces.Board;
import com.example.trylma.server.ServerPacket;

public class BoardUpdatePacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final Board board;
    private final ArrayList<String> movedPerformedByPlayers;

    public BoardUpdatePacket(Board board) {
        this.board = board;
        this.movedPerformedByPlayers = new ArrayList<>(board.getMovesPerformedByPlayers());
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<String> getMovedPerformedByPlayers() {
        return movedPerformedByPlayers;
    }
}
