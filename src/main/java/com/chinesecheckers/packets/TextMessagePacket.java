package com.chinesecheckers.packets;

import com.chinesecheckers.server.ServerPacket;

public class TextMessagePacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final String messageString;

    public TextMessagePacket(String messageString) {
        this.messageString = messageString;
    }

    public String getMessageString() {
        return messageString;
    }

    @Override
    public PacketType getType() {
        return PacketType.TEXT_MESSAGE;
    }
}
