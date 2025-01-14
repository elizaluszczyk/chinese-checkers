package com.example.trylma.game;

public enum GameType {
    DEFAULT;

    public static boolean isValid(String input) {
        for (GameType variant : GameType.values()) {
            if (variant.name().equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }
}

