package com.example.trylma.packets;

import com.example.trylma.server.ServerPacket;

public class UsernamePacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final String username;

    public UsernamePacket(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
