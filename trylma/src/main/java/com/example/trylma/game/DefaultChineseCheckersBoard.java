package com.example.trylma.game;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.trylma.board.Move;
import com.example.trylma.board.Pawn;
import com.example.trylma.interfaces.Board;
import com.example.trylma.interfaces.Player;

public class DefaultChineseCheckersBoard implements Board, Serializable {
    private static final long serialVersionUID = 1L;
    private final ArrayList<String> movesPerformedByPlayers;
    
    // 2D ArrayList to hold board state, using null for empty slots
    private final ArrayList<ArrayList<Pawn>> board;

    public DefaultChineseCheckersBoard(int numberOfPlayers) {
        this.board = new ArrayList<>();
        this.movesPerformedByPlayers = new ArrayList<>();
    }

    @Override
    public ArrayList<ArrayList<Pawn>> getBoard() {
        return board;
    }

    @Override
    public void addMoveTakenByPlayer(Player player, Move move) {
        String moveDetails = String.format(
            "Player <%s> moved from (%d, %d) to (%d, %d)",
            player.getUsername(), move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());

        movesPerformedByPlayers.add(moveDetails);
    }

    @Override
    public boolean isInBoard(int x, int y) {
        // For simplicity, assuming all coordinates are within bounds
        return true;
    }

    @Override
    public ArrayList<String> getMovesPerformedByPlayers() {
        return movesPerformedByPlayers;
    }
}
