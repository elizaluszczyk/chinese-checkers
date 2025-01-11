package com.example.trylma.interfaces;

import java.util.ArrayList;

import com.example.trylma.board.Field;
import com.example.trylma.board.Move;
import com.example.trylma.board.Pawn;
public interface Board {
    ArrayList<ArrayList<Field>> getBoard();

}