package com.chinesecheckers.packets;

import com.chinesecheckers.server.ServerPacket;

public class RequestUsernamePacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final String message;

    public RequestUsernamePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public PacketType getType() {
        return PacketType.REQUEST_USERNAME;
    }
}
