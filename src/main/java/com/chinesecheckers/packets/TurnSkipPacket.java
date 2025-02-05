package com.chinesecheckers.packets;

import com.chinesecheckers.server.ServerPacket;

public class TurnSkipPacket implements ServerPacket {
    @Override
    public PacketType getType() {
        return PacketType.TURN_SKIP;
    }
}
