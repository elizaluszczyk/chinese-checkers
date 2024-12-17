package com.example.trylma.packets;

import com.example.trylma.ServerPacket;

public class TextMessagePacket implements ServerPacket {
    private final String messageString;

    public TextMessagePacket(String messageString) {
        this.messageString = messageString;
    }

    public String getMessageString() {
        return messageString;
    }
}
