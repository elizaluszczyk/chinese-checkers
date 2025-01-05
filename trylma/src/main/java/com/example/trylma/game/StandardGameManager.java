package com.example.trylma.game;

import com.example.trylma.board.Move;
import com.example.trylma.factories.BoardFactory;
import com.example.trylma.interfaces.Board;
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
        return true; //TODO
    }

    @Override
    public void applyMove(Move move) {
        //TODO
    }
    
}
