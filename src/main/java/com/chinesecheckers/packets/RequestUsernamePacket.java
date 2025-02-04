package com.example.trylma.packets;

import com.example.trylma.server.ServerPacket;

public class RequestUsernamePacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final String message;

    public RequestUsernamePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
