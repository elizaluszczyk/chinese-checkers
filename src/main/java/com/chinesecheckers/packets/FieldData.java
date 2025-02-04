package com.chinesecheckers.packets;

import java.io.Serializable;

public class FieldData implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int x;
    private final int y;
    private final boolean active;
    private final boolean occupied;
    private final String ownerId;

    public FieldData(int x, int y, boolean active, boolean occupied, String ownerId) {
        this.x = x;
        this.y = y;
        this.active = active;
        this.occupied = occupied;
        this.ownerId = ownerId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public boolean isActive() {
        return active;
    }
    
    public boolean isOccupied() {
        return occupied;
    }
    
    @Override
    public String toString() {
        if (!active) {
            return " ";
        } else if (!occupied){
            return "*";
        } else {
            return "@";
        }
    }
}

