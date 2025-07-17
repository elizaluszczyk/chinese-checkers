package com.chinesecheckers.packets;

import com.chinesecheckers.server.ServerPacket;

public class TurnUpdatePacket implements ServerPacket {
  private final String message;

  public TurnUpdatePacket(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public PacketType getType() {
    return PacketType.TURN_UPDATE;
  }
}
