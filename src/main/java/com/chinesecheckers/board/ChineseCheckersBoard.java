package com.chinesecheckers.board;

import java.io.Serializable;
import java.util.ArrayList;

import com.chinesecheckers.interfaces.Board;
import com.chinesecheckers.interfaces.Field;

public class ChineseCheckersBoard implements Board, Serializable {
    private static final long serialVersionUID = 1L;

    private final int sizeOfHexagon;
    private final int columns;
    private final int rows;
    
    private final ArrayList<ArrayList<Field>> board;
    private ArrayList<ArrayList<Field>> armsOfStar;

    public ChineseCheckersBoard(int sizeOfHexagon) {
        this.sizeOfHexagon = sizeOfHexagon;
        this.columns = 6 * sizeOfHexagon - 5;
        this.rows = 4 * sizeOfHexagon - 3;
        this.board = new ArrayList<>();
        initializeBoard();
        markActiveFields();
        this.armsOfStar = createArmsOfStar();
    }

    private void initializeBoard() {
        for (int y = 0; y < rows; y++) {
            ArrayList<Field> row = new ArrayList<>();
            for (int x = 0; x < columns; x++) {
                row.add(new StandardField(y, x)); // FIXME
            }
            board.add(row);
        }
    }

    private void markActiveFields() {
        int inactiveStart = 0;
        int activeEnd = columns;

        for (int y = (sizeOfHexagon - 1); y < rows; y++) {
            for (int x = inactiveStart; x < activeEnd; x += 2) {
                board.get(y).get(x).setActive(true);
            }
            activeEnd--;
            inactiveStart++;
        }

        inactiveStart = 0;
        activeEnd = columns;

        for (int y = rows - (sizeOfHexagon); y >= 0; y--) {
            for (int x = inactiveStart; x < activeEnd; x += 2) {
                board.get(y).get(x).setActive(true);
            }
            activeEnd--;
            inactiveStart++;
        }
    }
    
    private ArrayList<ArrayList<Field>> getFieldsInArmsOfStar(ArrayList<ArrayList<Field>> board) {
        ArrayList<Field> starArm1 = new ArrayList<>();
        for (int y = 0; y < sizeOfHexagon - 1; y++) {
            for (Field field : board.get(y)) {
                if (field.isActive()) {
                    starArm1.add(field);
                }
            }
        }
        armsOfStar.add(starArm1);
        
        int numberOfInactiveFields2 = 0;
        int numberOfActiveFields2 = 2 * sizeOfHexagon - 2;
        
        ArrayList<Field> starArm2 = new ArrayList<>();
        for (int y = sizeOfHexagon - 1; y < (2 * sizeOfHexagon - 1); y++) {
            for (int x = numberOfInactiveFields2; x < numberOfActiveFields2; x++) {
                if (board.get(y).get(x).isActive()) {
                    starArm2.add(board.get(y).get(x));
                }
            }
            numberOfActiveFields2--;
            numberOfInactiveFields2++;

        }
        armsOfStar.add(starArm2);
        
        int numberOfActiveFields3 = 2 * sizeOfHexagon - 2;
        int numberOfInactiveFields3 = 0;
        ArrayList<Field> starArm3 = new ArrayList<>();
        for (int y = (3 * sizeOfHexagon - 3); y > (2 * sizeOfHexagon - 2); y--) {
            for (int x = numberOfInactiveFields3; x < numberOfActiveFields3; x++) {
                if (board.get(y).get(x).isActive()) {
                    starArm3.add(board.get(y).get(x));
                }
            }
            numberOfActiveFields3--;
            numberOfInactiveFields3++;
        }
        armsOfStar.add(starArm3);
        
        ArrayList<Field> starArm4 = new ArrayList<>();
        for (int y = rows + 1 - sizeOfHexagon; y < rows; y++) {
            for (Field field : board.get(y)) {
                if (field.isActive()) {
                    starArm4.add(field);
                }
            }
        }
        armsOfStar.add(starArm4);
        
        int numberOfInactiveFields5 = 0;
        ArrayList<Field> starArm5 = new ArrayList<>();
        for (int y = (3 * sizeOfHexagon - 3); y > (2 * sizeOfHexagon - 2); y--) {
            for (int x = 4 * sizeOfHexagon + numberOfInactiveFields5 - 3; x < columns; x++) {
                if (board.get(y).get(x).isActive()) {
                    starArm5.add(board.get(y).get(x));
                }
            }
            numberOfInactiveFields5++;
        }
        armsOfStar.add(starArm5);

        int numberOfInactiveFields6 = 0;
        ArrayList<Field> starArm6 = new ArrayList<>();
        for (int y = sizeOfHexagon - 1; y < (2 * sizeOfHexagon - 1); y++) {
            for (int x = 4 * sizeOfHexagon - 3 + numberOfInactiveFields6; x < columns; x++) {
                if (board.get(y).get(x).isActive()) {
                    starArm6.add(board.get(y).get(x));
                }
            }
            numberOfInactiveFields6++;
        }
        armsOfStar.add(starArm6);
        
        return armsOfStar;
    }
    
    @Override
    public ArrayList<ArrayList<Field>> getBoard() {
        return board;
    }
    
    @Override
    public Field getField(int x, int y) {
        if (y >= 0 && y < board.size() && x >= 0 && x < board.get(y).size()) {
            return board.get(y).get(x);
        }
        return null;
    }

    public ArrayList<ArrayList<Field>> getArmsOfStar() {
        return this.armsOfStar;
    }
}
