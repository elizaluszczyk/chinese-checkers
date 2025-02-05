package com.chinesecheckers.packets;

import com.chinesecheckers.server.ServerPacket;

public class TextMessagePacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final String message;

    public TextMessagePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public PacketType getType() {
        return PacketType.TEXT_MESSAGE;
    }

    @Override
    public PacketType getType() {
        return PacketType.TEXT_MESSAGE;
    }
}
