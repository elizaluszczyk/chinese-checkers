package com.example.trylma.gui;

import com.example.trylma.interfaces.Field;

import javafx.scene.shape.Circle;

public class CircleField extends Circle {
    private double centerX;
    private double centerY;
    private Field field = null;
    private CirclePawn pawn = null;
    
    public CircleField(double x, double y, int size) {
        super(x, y,size);
    }

    public void setField(Field field) {
        this.field = field;
    }

    public CirclePawn getPawn() {
        return pawn;
    }

    public void setPawn(CirclePawn pawn) {
        this.pawn = pawn;
    }

    public double getCenterXCircleField() {
        return centerX;
    }

    public double getCenterYCircleField() {
        return centerY;
    }
}
