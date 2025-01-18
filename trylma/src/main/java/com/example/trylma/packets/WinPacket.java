package com.example.trylma.packets;

import com.example.trylma.server.ServerPacket;

public class WinPacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final String message;

    public WinPacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
