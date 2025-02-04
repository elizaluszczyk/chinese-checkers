package com.example.trylma.game;

public enum GameType {
    DEFAULT,
    YINANDYANG,
    DEFAULTWITHBOT;

    public static GameType fromString(String input) {
        for (GameType variant : GameType.values()) {
            if (variant.name().equalsIgnoreCase(input)) {
                return variant;
            }
        }
        return null;
    }
}
