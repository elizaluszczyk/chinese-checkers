package com.example.trylma.interfaces;


public interface Board {

    void initializeBoard();

    void setPosition(int row, int col, String value);

    String getPosition(int row, int col);

    void displayBoard();

    void addPlayer(Player player);

}
