package com.example.trylma;


import com.example.trylma.board.Pawn;
import javafx.scene.shape.Circle;

public class CirclePawn extends Circle {
    private Pawn pawn = null;
    public CirclePawn(double x, double y, int size) {
        super(x, y,size);

    }

    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }
}
