package com.chinesecheckers.board;

import java.io.Serializable;

public class Move implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;

    public Move(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    @Override
    public String toString() {
        return "MOVE " + startX + "," + startY + " TO " + endX + "," + endY;
    }
}
