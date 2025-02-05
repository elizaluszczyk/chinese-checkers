package com.chinesecheckers.server;

import java.io.Serializable;

import com.chinesecheckers.packets.PacketType;

public interface ServerPacket extends Serializable {
    PacketType getType();
}
