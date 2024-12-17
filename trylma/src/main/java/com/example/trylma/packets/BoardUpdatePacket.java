package com.example.trylma.packets;

import java.util.ArrayList;

import com.example.trylma.ServerPacket;
import com.example.trylma.interfaces.Board;

public class BoardUpdatePacket implements ServerPacket {
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
