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
                row.add(new StandardField(x, y));
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
    
    private ArrayList<ArrayList<Field>> createArmsOfStar() {
        ArrayList<ArrayList<Field>> arms = new ArrayList<>();
        
        // arm 1 (top)
        arms.add(collectActiveFieldsInRows(0, sizeOfHexagon - 1));
        
        // arm 2 (top-left)
        arms.add(collectTopLeftArm());
        
        // arm 3 (bottom-left)
        arms.add(collectBottomLeftArm());
        
        // arm 4 (bottom)
        arms.add(collectActiveFieldsInRows(rows + 1 - sizeOfHexagon, rows));
        
        // arm 5 (bottom-right)
        arms.add(collectBottomRightArm());
        
        // arm 6 (top-right)
        arms.add(collectTopRightArm());
        
        return arms;
    }
    
    private ArrayList<Field> collectActiveFieldsInRows(int startRow, int endRow) {
        ArrayList<Field> fields = new ArrayList<>();
        for (int y = startRow; y < endRow; y++) {
            for (Field field : board.get(y)) {
                if (field.isActive()) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }
    
    private ArrayList<Field> collectTopLeftArm() {
        ArrayList<Field> arm = new ArrayList<>();
        int inactiveStart = 0;
        int activeEnd = 2 * sizeOfHexagon - 2;
        
        for (int y = sizeOfHexagon - 1; y < (2 * sizeOfHexagon - 1); y++) {
            for (int x = inactiveStart; x < activeEnd; x++) {
                if (board.get(y).get(x).isActive()) {
                    arm.add(board.get(y).get(x));
                }
            }
            activeEnd--;
            inactiveStart++;
        }
        return arm;
    }
    
    private ArrayList<Field> collectBottomLeftArm() {
        ArrayList<Field> arm = new ArrayList<>();
        int activeEnd = 2 * sizeOfHexagon - 2;
        int inactiveStart = 0;
        
        for (int y = (3 * sizeOfHexagon - 3); y > (2 * sizeOfHexagon - 2); y--) {
            for (int x = inactiveStart; x < activeEnd; x++) {
                if (board.get(y).get(x).isActive()) {
                    arm.add(board.get(y).get(x));
                }
            }
            activeEnd--;
            inactiveStart++;
        }
        return arm;
    }
    
    private ArrayList<Field> collectBottomRightArm() {
        ArrayList<Field> arm = new ArrayList<>();
        int inactiveStart = 0;
        
        for (int y = (3 * sizeOfHexagon - 3); y > (2 * sizeOfHexagon - 2); y--) {
            for (int x = 4 * sizeOfHexagon + inactiveStart - 3; x < columns; x++) {
                if (board.get(y).get(x).isActive()) {
                    arm.add(board.get(y).get(x));
                }
            }
            inactiveStart++;
        }
        return arm;
    }
    
    private ArrayList<Field> collectTopRightArm() {
        ArrayList<Field> arm = new ArrayList<>();
        int inactiveStart = 0;
        
        for (int y = sizeOfHexagon - 1; y < (2 * sizeOfHexagon - 1); y++) {
            for (int x = 4 * sizeOfHexagon - 3 + inactiveStart; x < columns; x++) {
                if (board.get(y).get(x).isActive()) {
                    arm.add(board.get(y).get(x));
                }
            }
            inactiveStart++;
        }
        return arm;
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
