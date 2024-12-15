package com.example.trylma.interfaces;


public interface Board {
    public ArrayList<ArrayList<Pawn>> getBoard();
    public void addMoveTakenByPlayer(Player player, Move move);
    public boolean isInBoard(int x, int y)
    public ArrayList<String> getMovesPerformedByPlayers();

}
