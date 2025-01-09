package com.example.trylma.game;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.trylma.interfaces.Board;
import com.example.trylma.interfaces.Field;

public class ChineseCheckersBoard implements Board, Serializable {
    private static final long serialVersionUID = 1L;
    private final ArrayList<ArrayList<Field>> board;

    public ChineseCheckersBoard(int numberOfPlayers) {
        this.board = new ArrayList<>();
    }

    @Override
    public ArrayList<ArrayList<Field>> getBoard() {
        return board;
    }

    @Override
    public Field getField(int x, int y) {
        if (x >= 0 && x < board.size() && y >= 0 && y < board.get(x).size()) {
            return board.get(x).get(y);
        }
        return null;
    }
}
