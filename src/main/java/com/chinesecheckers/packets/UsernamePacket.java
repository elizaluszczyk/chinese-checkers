package com.chinesecheckers.packets;

import com.chinesecheckers.server.ServerPacket;

public class UsernamePacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final String username;

    public UsernamePacket(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public PacketType getType() {
        return PacketType.USERNAME;
    }
}
