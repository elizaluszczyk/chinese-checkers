package com.example.trylma.game;

import java.util.ArrayList;

import com.example.trylma.board.ChineseCheckersBoard;
import com.example.trylma.board.Move;
import com.example.trylma.board.Pawn;
import com.example.trylma.interfaces.Field;

public class BotPlayer extends GamePlayer {
    private final Random random = new Random();

    public BotPlayer(String username) {
        super(username);
    }

    public Move generateMove(ChineseCheckersBoard board) {
        ArrayList<Move> possibleMoves = getPossibleNeighbourFieldsMove(board);
        System.out.println("Possible moves: " + possibleMoves);

        if (possibleMoves.isEmpty()) {
            return null; 
        }

        int index = random.nextInt(possibleMoves.size());
        return possibleMoves.get(index);
    }

    private ArrayList<Move> getPossibleNeighbourFieldsMove(ChineseCheckersBoard board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();

        for (Pawn pawn : this.getPawns()) {
            Field currentField = pawn.getCurrentField();

            ArrayList<Move> movesForCurrentField = generateMovesForField(board, currentField);
            System.out.println(("For field " + currentField.getX() + ", " + currentField.getY() + " are possible moves: " + movesForCurrentField));

            
            for (Move move : movesForCurrentField) {
                    possibleMoves.add(move);
            }
            
        }

        return possibleMoves;
    }

    private ArrayList<Move> generateMovesForField(ChineseCheckersBoard board, Field field) {
        ArrayList<Move> moves = new ArrayList<>();

        int[][] directions = {
            {1, -1}, 
            {-1, -1}, 
            {2, 0},
            {-1, 1}, 
            {1, 1},
            {-2, 0}  
        };

        for (int[] direction : directions) {
            int targetX = field.getX() + direction[0];
            int targetY = field.getY() + direction[1];

            Field targetField = board.getField(targetY, targetX);
            if (targetField != null && targetField.isActive() && !targetField.isOccupied()) {
                moves.add(new Move(field.getX(), field.getY(), targetX, targetY));
            }
        }
        return moves;
    }

    private ArrayList<Move> generateJumpMovesForField(ChineseCheckersBoard board, Field field) {
        ArrayList<Move> jumpMoves = new ArrayList<>();
        int[][] directions = {
            {4, 0}, {-4, 0}, {2, 2},
            {-2, -2}, {2, -2}, {-2, 2}
        };

        for (int[] direction : directions) {
            int targetX = field.getX() + direction[0];
            int targetY = field.getY() + direction[1];

            Field targetField = board.getField(targetY, targetX);
            if (targetField != null && targetField.isActive() && !targetField.isOccupied()) {
                int midX = (field.getX() + targetX) / 2;
                int midY = (field.getY() + targetY) / 2;

                Field middleField = board.getField(midY, midX);
                if (middleField != null && middleField.isOccupied() && middleField.isActive()) {
                    jumpMoves.add(new Move(field.getX(), field.getY(), targetX, targetY));
                }
            }
        }
        return jumpMoves;
    }
}
