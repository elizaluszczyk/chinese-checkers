package com.example.trylma.packets;

import com.example.trylma.server.ServerPacket;

public class TurnUpdatePacket implements ServerPacket {
    private final String message;

    public TurnUpdatePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
