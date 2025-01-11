package com.example.trylma.board;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.trylma.interfaces.Board;
import com.example.trylma.interfaces.Player;

public class ChineseCheckersBoard implements Board, Serializable {
    private static final long serialVersionUID = 1L;
    private final ArrayList<String> movesPerformedByPlayers;

    private final ArrayList<ArrayList<Field>> board;
    private final int columns;
    private final int rows;
    private ArrayList<ArrayList<Field>> armsOfStar = new ArrayList<>();

    public ArrayList<ArrayList<Field>> getArmsOfStar() {
        return this.armsOfStar;
    }

    private final int hexagon;

    public ChineseCheckersBoard(int sizeOfHexagon) {
        this.hexagon = sizeOfHexagon;
        this.columns = 6 * sizeOfHexagon - 5;
        this.rows = 4 * sizeOfHexagon - 3;
        this.board = new ArrayList<>();
        this.movesPerformedByPlayers = new ArrayList<>();
        initializeBoard();
        initializeFields(board);
        this.armsOfStar = getFieldsInArmsOfStar(board);
    }

    public void initializeBoard() {
        for (int y = 0; y < rows; y++) {
            ArrayList<Field> row = new ArrayList<>();
            for (int x = 0; x < columns; x++) {
                row.add(new Field(x, y));
            }
            board.add(row);
        }
    }

    public void initializeFields(ArrayList<ArrayList<Field>> board) {
        int numberOfInactiveFields1 = 0;
        int numberOfActiveFields1 = columns;
        for (int y = (hexagon - 1); y < rows; y++) {
            for (int x = numberOfInactiveFields1; x < numberOfActiveFields1; x += 2) {
                board.get(y).get(x).setActive(true);
            }
            numberOfActiveFields1--;
            numberOfInactiveFields1++;
        }
        int numberOfActiveFields2 = columns;
        int numberOfInactiveFields2 = 0;
        for (int y = rows - (hexagon); y >= 0; y--) {
            for (int x = numberOfInactiveFields2; x < numberOfActiveFields2; x += 2) {
                board.get(y).get(x).setActive(true);
            }
            numberOfActiveFields2--;
            numberOfInactiveFields2++;
        }
    }

    public ArrayList<ArrayList<Field>> getFieldsInArmsOfStar(ArrayList<ArrayList<Field>> board) {
        ArrayList<Field> starArm1 = new ArrayList<>();
        for (int y = 0; y < hexagon - 1; y++) {
            for (Field field : board.get(y)) {
                if (field.isActive()) {
                    starArm1.add(field);
                }
            }
        }
        armsOfStar.add(starArm1);

        int numberOfInactiveFields2 = 0;
        int numberOfActiveFields2 = 2 * hexagon - 2;

        ArrayList<Field> starArm2 = new ArrayList<>();
        for (int y = hexagon - 1; y < (2 * hexagon - 1); y++) {
            for (int x = numberOfInactiveFields2; x < numberOfActiveFields2; x++) {
                if (board.get(y).get(x).isActive()) {
                    starArm2.add(board.get(y).get(x));
                }
            }
            numberOfActiveFields2--;
            numberOfInactiveFields2++;

        }
        armsOfStar.add(starArm2);

        int numberOfActiveFields3 = 2 * hexagon - 2;
        int numberOfInactiveFields3 = 0;
        ArrayList<Field> starArm3 = new ArrayList<>();
        for (int y = (3 * hexagon - 3); y > (2 * hexagon - 2); y--) {
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
        for (int y = rows + 1 - hexagon; y < rows; y++) {
            for (Field field : board.get(y)) {
                if (field.isActive()) {
                    starArm4.add(field);
                }
            }
        }
        armsOfStar.add(starArm4);

        int numberOfInactiveFields5 = 0;
        ArrayList<Field> starArm5 = new ArrayList<>();
        for (int y = (3 * hexagon - 3); y > (2 * hexagon - 2); y--) {
            for (int x = 4 * hexagon + numberOfInactiveFields5 - 3; x < columns; x++) {
                if (board.get(y).get(x).isActive()) {
                    starArm5.add(board.get(y).get(x));
                }
            }
            numberOfInactiveFields5++;
        }
        armsOfStar.add(starArm5);


        int numberOfInactiveFields6 = 0;
        ArrayList<Field> starArm6 = new ArrayList<>();
        for (int y = hexagon - 1; y < (2 * hexagon - 1); y++) {
            for (int x = 4 * hexagon - 3 + numberOfInactiveFields6; x < columns; x++) {
                if (board.get(y).get(x).isActive()) {
                    starArm6.add(board.get(y).get(x));
                }
            }
            numberOfInactiveFields6++;
        }
        armsOfStar.add(starArm6);

        return armsOfStar;

    }

    public void printStartingPositions() {
        for (int i = 0; i < armsOfStar.size(); i++) {
            System.out.println("Współrzędne z ramiona nr " + (i + 1));
            for (int j = 0; j < armsOfStar.get(i).size(); j++) {
                System.out.println("pole: (" + armsOfStar.get(i).get(j).getX() + ", " + armsOfStar.get(i).get(j).getY() + " )");
            }
        }
    }


    @Override
    public ArrayList<ArrayList<Field>> getBoard() {
        return board;
    }


//    public void printBoard() {
//        for (List<Field> row : board) {
//            for (Field field : row) {
//                System.out.print(field.getActive() ? field.toString() : "{ }");
//            }
//            System.out.println();
//        }
//    }
}




