package com.chinesecheckers.game;

import com.chinesecheckers.board.ChineseCheckersBoard;
import com.chinesecheckers.board.Move;
import com.chinesecheckers.board.Pawn;
import com.chinesecheckers.interfaces.Field;
import java.util.ArrayList;
import java.util.Random;

public class BotPlayer extends GamePlayer {

  public BotPlayer(String username) {
    super(username);
  }

  public Move generateMove(ChineseCheckersBoard board) {
    ArrayList<Move> candidateMoves = new ArrayList<>();

    for (Pawn pawn : this.getPawns()) {
      Field currentField = pawn.getCurrentField();
      ArrayList<Move> regularMoves = generateMovesForField(board, currentField);
      ArrayList<Move> jumpMoves = generateJumpMovesForField(board, currentField);
      ArrayList<Move> pawnMoves = new ArrayList<>();
      pawnMoves.addAll(regularMoves);
      pawnMoves.addAll(jumpMoves);

      if (pawnMoves.isEmpty()) {
        continue;
      }

      double minDistance = Double.MAX_VALUE;
      ArrayList<Move> bestPawnMoves = new ArrayList<>();

      for (Move move : pawnMoves) {
        Field targetField = board.getField(move.getEndX(), move.getEndY());
        if (targetField == null) continue;

        double currentMinDistance =
            this.getTargetPositions().stream()
                .mapToDouble(t -> calculateDistance(targetField, t))
                .min()
                .orElse(Double.MAX_VALUE);

        if (currentMinDistance < minDistance) {
          minDistance = currentMinDistance;
          bestPawnMoves.clear();
          bestPawnMoves.add(move);
        } else if (currentMinDistance == minDistance) {
          bestPawnMoves.add(move);
        }
      }

      if (!bestPawnMoves.isEmpty()) {
        bestPawnMoves.sort(
            (m1, m2) -> {
              double d1 =
                  calculateDistance(
                      board.getField(m1.getStartX(), m1.getStartY()),
                      board.getField(m1.getEndX(), m1.getEndY()));
              double d2 =
                  calculateDistance(
                      board.getField(m2.getStartX(), m2.getStartY()),
                      board.getField(m2.getEndX(), m2.getEndY()));
              return Double.compare(d2, d1);
            });

        candidateMoves.add(bestPawnMoves.get(0));
      }
    }

    if (candidateMoves.isEmpty()) {
      return null;
    }

    candidateMoves.sort(
        (m1, m2) -> {
          double d1 =
              calculateDistance(
                  board.getField(m1.getStartX(), m1.getStartY()),
                  board.getField(m1.getEndX(), m1.getEndY()));
          double d2 =
              calculateDistance(
                  board.getField(m2.getStartX(), m2.getStartY()),
                  board.getField(m2.getEndX(), m2.getEndY()));
          return Double.compare(d2, d1);
        });

    return selectWeightedRandomMove(candidateMoves, board);
  }

  private Move selectWeightedRandomMove(ArrayList<Move> moves, ChineseCheckersBoard board) {
    ArrayList<Double> weights = new ArrayList<>();
    double totalWeight = 0;

    for (int i = 0; i < moves.size(); i++) {
      Move move = moves.get(i);
      double baseWeight = moves.size() - i;

      Field startField = board.getField(move.getStartX(), move.getStartY());
      boolean isInTarget =
          this.getTargetPositions().stream()
              .anyMatch(t -> t.getX() == startField.getX() && t.getY() == startField.getY());

      double finalWeight = isInTarget ? baseWeight * 0.10 : baseWeight;
      weights.add(finalWeight);
      totalWeight += finalWeight;
    }

    double random = new Random().nextDouble() * totalWeight;
    double cumulative = 0;

    for (int i = 0; i < moves.size(); i++) {
      cumulative += weights.get(i);
      if (random < cumulative) {
        return moves.get(i);
      }
    }

    return moves.get(0);
  }

  private double calculateDistance(Field a, Field b) {
    return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
  }

  private ArrayList<Move> generateMovesForField(ChineseCheckersBoard board, Field field) {
    ArrayList<Move> moves = new ArrayList<>();
    int[][] directions = {
      {1, -1}, {-1, -1}, {2, 0},
      {-1, 1}, {1, 1}, {-2, 0}
    };

    for (int[] direction : directions) {
      int targetX = field.getX() + direction[0];
      int targetY = field.getY() + direction[1];

      Field targetField = board.getField(targetX, targetY);
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

      Field targetField = board.getField(targetX, targetY);
      if (targetField != null && targetField.isActive() && !targetField.isOccupied()) {
        int midX = (field.getX() + targetX) / 2;
        int midY = (field.getY() + targetY) / 2;

        Field middleField = board.getField(midX, midY);
        if (middleField != null && middleField.isOccupied() && middleField.isActive()) {
          jumpMoves.add(new Move(field.getX(), field.getY(), targetX, targetY));
        }
      }
    }
    return jumpMoves;
  }
}
