package com.example.trylma.interfaces;

public interface Pawn {
        String getOwner();
        void setOwner(String owner);
        String getColor();
        boolean isActive();
        void deactivate();
}
