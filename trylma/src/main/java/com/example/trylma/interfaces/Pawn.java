package com.example.chinese_checkers.interfaces;

public interface Pawn {
        String getOwner();
        void setOwner(String owner);
        String getColor();
        boolean isActive();
        void deactivate();
}
