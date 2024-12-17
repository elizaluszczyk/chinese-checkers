package com.example.trylma.interfaces;

import java.util.ArrayList;

import com.example.trylma.board.Move;
import com.example.trylma.board.Pawn;

public interface Board {
    ArrayList<ArrayList<Pawn>> getBoard();
    void addMoveTakenByPlayer(Player player, Move move);
    boolean isInBoard(int x, int y);
    ArrayList<String> getMovesPerformedByPlayers();

}
