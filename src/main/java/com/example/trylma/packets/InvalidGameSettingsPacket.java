package com.example.trylma.packets;

import com.example.trylma.server.ServerPacket;

public class InvalidGameSettingsPacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final String message;

    public InvalidGameSettingsPacket(String msg) {
        message = msg;
    }

    public String getMessage() {
        return message;
    }
}
