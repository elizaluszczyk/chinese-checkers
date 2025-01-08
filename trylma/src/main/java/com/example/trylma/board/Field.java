package com.example.trylma.board;
public class Field implements com.example.trylma.interfaces.Field {
    private boolean occupied;
    private final int x;
    private final int y;
    private FieldType type;
    private boolean isActive;
    private Pawn pawn;
    public Field(int x, int y, boolean isActive) {
        this.x = x;
        this.y = y;
        this.isActive = isActive;
        this.occupied = false;
        this.pawn = null;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
    public boolean isOccupied() {
        return occupied;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean getActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
    public FieldType getType() {
        return type;
    }
    public void setType(FieldType type) {
        this.type = type;
    }
    public boolean isEmpty() {
        return isActive && !occupied;
    }
    public Pawn getPawn() {
        return pawn;
    }

    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }
    public String toString() {
        if (!isActive) {
            return "{ }";
        } else {
            return "{*}";
        }
    }
}
