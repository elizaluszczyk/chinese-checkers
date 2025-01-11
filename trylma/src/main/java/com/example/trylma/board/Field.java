package com.example.trylma.board;
public class Field implements com.example.trylma.interfaces.Field {
    private boolean occupied;
    private final int x;
    private final int y;
//    private FieldType type;
    private boolean active = false;
    private Pawn pawn;
    public Field(int x, int y) {
        this.x = x;
        this.y = y;
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
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
//    public FieldType getType() {
//        return type;
//    }
//    public void setType(FieldType type) {
//        this.type = type;
//    }
    public boolean isEmpty() {
        return active && !occupied;
    }
    public Pawn getPawn() {
        return pawn;
    }

    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }
    public String toString() {
        if (!active) {
            return "{ }";
        } else if (!occupied){
            return "{*}";
        }else{
            return "{@}";
        }
    }
}
