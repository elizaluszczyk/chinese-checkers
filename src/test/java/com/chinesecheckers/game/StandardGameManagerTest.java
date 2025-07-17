package com.chinesecheckers.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.chinesecheckers.board.ChineseCheckersBoard;
import com.chinesecheckers.board.Move;
import com.chinesecheckers.board.Pawn;
import com.chinesecheckers.board.StandardField;
import com.chinesecheckers.interfaces.Field;
import com.chinesecheckers.interfaces.Player;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StandardGameManagerTest {

  private StandardGameManager gameManager;
  private ChineseCheckersBoard board;
  private ChineseCheckersBoard boardMock;
  private Player player;
  private Pawn pawn;

  @BeforeEach
  void setUp() {
    player = mock(Player.class);
    pawn = mock(Pawn.class);

    boardMock = mock(ChineseCheckersBoard.class);
    gameManager = new StandardGameManager("default", 2);
    board = gameManager.getBoard();
    gameManager = spy(gameManager);
  }

  @Test
  void testIsMoveValid_InactiveFields() {
    Move move = new Move(0, 0, 1, 1);
    Field startField = mock(Field.class);
    Field endField = mock(Field.class);
    when(boardMock.getField(0, 0)).thenReturn(startField);
    when(boardMock.getField(1, 1)).thenReturn(endField);
    when(startField.isActive()).thenReturn(false);
    when(endField.isActive()).thenReturn(false);
    assertFalse(gameManager.isMoveValid(move, player));
  }

  @Test
  void testIsMoveValid_UnoccupiedOrOccupiedFields() {
    Move move = new Move(0, 0, 1, 1);
    Field startField = mock(Field.class);
    Field endField = mock(Field.class);

    when(boardMock.getField(0, 0)).thenReturn(startField);
    when(boardMock.getField(1, 1)).thenReturn(endField);
    when(startField.isActive()).thenReturn(true);
    when(endField.isActive()).thenReturn(true);
    when(startField.isOccupied()).thenReturn(false);
    when(endField.isOccupied()).thenReturn(true);

    assertFalse(gameManager.isMoveValid(move, player));
  }

  @Test
  void testIsMoveValid_PawnNotBelongingToPlayer() {
    Move move = new Move(0, 0, 1, 1);
    Field startField = mock(Field.class);
    Field endField = mock(Field.class);

    when(boardMock.getField(0, 0)).thenReturn(startField);
    when(boardMock.getField(1, 1)).thenReturn(endField);
    when(startField.isActive()).thenReturn(true);
    when(endField.isActive()).thenReturn(true);
    when(startField.isOccupied()).thenReturn(true);
    when(endField.isOccupied()).thenReturn(false);
    when(startField.getPawn()).thenReturn(pawn);
    ArrayList<Pawn> mockPawnList = new ArrayList<>();
    when(player.getPawns()).thenReturn(mockPawnList);
    assertFalse(gameManager.isMoveValid(move, player));
  }

  @Test
  void testApplyMove() {
    Move move = new Move(12, 8, 13, 9);
    Field startField = board.getField(12, 8);
    Field endField = board.getField(13, 9);
    startField.setPawn(pawn);
    startField.setOccupied(true);
    gameManager.applyMove(move);

    assertFalse(startField.isOccupied(), "Start field should be unoccupied after move.");
    assertNull(startField.getPawn(), "Start field should have no pawn after move.");
    assertEquals(pawn, endField.getPawn(), "End field should have the pawn after move.");
    assertTrue(endField.isOccupied(), "End field should be occupied after move.");
  }

  @Test
  void testIsWinningMove() {
    Field targetField = new StandardField(0, 0);
    Pawn playerPawn = mock(Pawn.class);

    when(playerPawn.getCurrentField()).thenReturn(targetField);
    when(player.getPawns()).thenReturn(new ArrayList<>(List.of(playerPawn)));
    when(player.getTargetPositions()).thenReturn(new ArrayList<>(List.of(targetField)));

    assertTrue(
        gameManager.isWinningMove(player),
        "Gracz powinien wygrać, jeśli wszystkie pionki są na polach docelowych.");
  }
}
