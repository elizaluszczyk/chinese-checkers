package com.chinesecheckers.packets;

import com.chinesecheckers.server.ServerPacket;

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
