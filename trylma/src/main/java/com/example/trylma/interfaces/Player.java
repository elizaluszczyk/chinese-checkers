package com.example.trylma.interfaces;

public interface Player {
    String getUsername();
    String getColor();
    void setColor(String color);
    boolean isActive();
    void deactivate();
}
