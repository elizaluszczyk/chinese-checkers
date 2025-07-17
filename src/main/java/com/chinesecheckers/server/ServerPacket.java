package com.chinesecheckers.server;

import com.chinesecheckers.packets.PacketType;
import java.io.Serializable;

public interface ServerPacket extends Serializable {
  PacketType getType();
}
