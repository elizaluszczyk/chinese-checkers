package com.example.trylma.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import com.example.trylma.board.ChineseCheckersBoard;
import com.example.trylma.board.Move;
import com.example.trylma.board.Pawn;
import com.example.trylma.factories.BoardFactory;
import com.example.trylma.interfaces.Field;
import com.example.trylma.interfaces.GameManager;
import com.example.trylma.interfaces.Player;

public class StandardGameManager implements GameManager {
    private final ChineseCheckersBoard board;

    public StandardGameManager(String gameType, int numberOfPlayers) {
        this.board = BoardFactory.createBoard(gameType, numberOfPlayers);
    }

    @Override
    public ChineseCheckersBoard getBoard() {
        return board;
    }

    @Override
    public boolean isMoveValid(Move move, Player player) {
        Field startField = board.getField(move.getStartX(), move.getStartY());
        Field endField = board.getField(move.getEndX(), move.getEndY());

        if (startField == null || endField == null) {
            return false;
        }

        if (!startField.isActive() || !endField.isActive()) {
            return false;
        }

        if (!startField.isOccupied() || endField.isOccupied()) {
            return false;
        }

        if (!player.getPawns().contains(startField.getPawn())) {
            return false;
        }

        int dx = Math.abs(move.getEndX() - move.getStartX());
        int dy = Math.abs(move.getEndY() - move.getStartY());

        // move to the neighboring field
        if ((dx == 0 && dy == 2) || (dx == 1 && dy == 1)) {
            return true;
        }

        // one or multi jump
        return isValidJump(move.getStartX(), move.getEndX(), move.getStartY(), move.getEndY());
    }

    @Override
    public void applyMove(Move move) {
        Field startField = board.getField(move.getStartX(), move.getStartY());
        Field endField = board.getField(move.getEndX(), move.getEndY());

        endField.setOccupied(true);
        endField.setPawn(startField.getPawn());
        startField.setOccupied(false);
        startField.setPawn(null);
    }

    @Override
    public void applyMoveForBot(Move move) {
        Field startField = board.getField(move.getStartY(), move.getStartX());
        Field endField = board.getField(move.getEndY(), move.getEndX());

        Pawn movingPawn = startField.getPawn(); 
        if (movingPawn != null) {
            endField.setPawn(movingPawn); 
            movingPawn.setCurrentField(endField); 
        }

        endField.setOccupied(true);
        startField.setOccupied(false);
        startField.setPawn(null);
    }

    @Override
    public boolean isWinningMove(Player player) {
        boolean allPawnsOnTarget = true;

        for (Pawn pawn : player.getPawns()) {
            Field pawnField = pawn.getCurrentField();

            ArrayList<Field> targetPositions = player.getTargetPositions();

            boolean isOnTarget = false;
            for (Field targetField : targetPositions) {
                if (pawnField.equals(targetField)) {
                    isOnTarget = true;
                    break;
                }
            }

            if (!isOnTarget) {
                allPawnsOnTarget = false;
                break; 
            }
        }
        
        return allPawnsOnTarget;
    }

    public boolean isValidJump(int startX, int endX, int startY, int endY) {
        Queue<int[]> queue = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();

        queue.add(new int[]{startX, startY});
        visited.add(startX + "," + startY);

        int[][] directions = {
            {0, 4}, {0, -4}, {2, 2}, {-2, -2}, {2, -2}, {-2, 2}
        };

        while(!queue.isEmpty()) {
            int[] current = queue.poll();
            int currX = current[0];
            int currY = current[1];

            for (int[] direction : directions) {
                int nextX = currX + direction[0];
                int nextY = currY + direction[1];
                int midX = (currX + nextX) / 2;
                int midY = (currY + nextY) / 2;

                Field middleField = board.getField(midX, midY);
                Field nextField = board.getField(nextX, nextY);

                if (nextField == null || middleField == null) {
                    continue;
                }

                if (!nextField.isActive() || nextField.isOccupied()) {
                    continue;
                }

                if (!middleField.isActive() || !middleField.isOccupied()) {
                    continue;
                }

                if (nextX == endX && nextY == endY) {
                    return true;
                }

                String position = nextX + "," + nextY;
                if(!visited.contains(position)) {
                    queue.add(new int[]{nextX, nextY});
                    visited.add(position);
                }
            }
        }

        return false;
    }
}
