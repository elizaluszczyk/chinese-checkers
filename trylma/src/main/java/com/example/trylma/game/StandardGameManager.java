package com.example.trylma.game;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import com.example.trylma.board.Move;
import com.example.trylma.factories.BoardFactory;
import com.example.trylma.interfaces.Board;
import com.example.trylma.interfaces.Field;
import com.example.trylma.interfaces.GameManager;

public class StandardGameManager implements GameManager {
    private final Board board;

    public StandardGameManager(String gameType, int numberOfPlayers) {
        this.board = BoardFactory.createBoard(gameType, numberOfPlayers);

    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public boolean isMoveValid(Move move) {
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

        int dx = Math.abs(move.getEndX() - move.getStartX());
        int dy = Math.abs(move.getEndY() - move.getStartY());

        // move to the neighboring field
        if ((dx == 2 && dy == 0) || (dx == 1 && dy == 1)) {
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

    private boolean isValidJump(int startX, int endX, int startY, int endY) {
        Queue<int[]> queue = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();

        queue.add(new int[]{startX, startY});
        visited.add(startX + "," + startY);

        int[][] directions = {
            {4, 0}, {-4, 0}, {2, 2}, {-2, -2}, {2, -2}, {-2, 2}
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
