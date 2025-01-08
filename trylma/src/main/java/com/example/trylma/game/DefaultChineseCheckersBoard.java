package com.example.trylma.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.trylma.board.Field;
import com.example.trylma.board.Move;
import com.example.trylma.board.Pawn;
import com.example.trylma.interfaces.Board;
import com.example.trylma.interfaces.Player;

public class DefaultChineseCheckersBoard implements Board, Serializable {
    private static final long serialVersionUID = 1L;
    private final ArrayList<String> movesPerformedByPlayers;

    private final ArrayList<ArrayList<Field>> board;
    private final int columns = 25;
    private final int rows = 17;
    public DefaultChineseCheckersBoard() {
        this.board = new ArrayList<>();
        this.movesPerformedByPlayers = new ArrayList<>();
        initializeBoard();
        initializeFields(board);
    }

    public void initializeBoard() {
        for (int y = 0; y < rows; y++) {
            ArrayList<Field> row = new ArrayList<>();
            for (int x = 0; x < columns; x++) {
                row.add(new Field(x, y, false));
            }
            board.add(row);
//            System.out.println(row.toString());
        }
    }
    public void initializeFields(ArrayList<ArrayList<Field>> board) {
        int numberOfInactiveFields1 = 0;
        int numberOfActiveFields1=columns;
        for (int y = 4; y < rows; y++) {
            for (int x = numberOfInactiveFields1; x< numberOfActiveFields1; x+=2) {
                board.get(y).get(x).setActive(true);
            }
            numberOfActiveFields1--;
            numberOfInactiveFields1++;
        }
        int numberOfActiveFields2 = 1;
        int numberOfInactiveFields2 = (columns-numberOfActiveFields2)/2;
        for (int y = 0; y < rows-4; y++) {
            for (int x = numberOfInactiveFields2; x< numberOfInactiveFields1+numberOfActiveFields2; x+=2) {
                board.get(y).get(x).setActive(true);
            }
            numberOfActiveFields2++;
            numberOfInactiveFields2--;
        }
    }



    @Override
    public ArrayList<ArrayList<Field>> getBoard() {
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
    public void printBoard() {
        for (List<Field> row : board) {
            for (Field field : row) {
                System.out.print(field.getActive() ? field.toString() : "{ }");
            }
            System.out.println();
        }
    }

}

