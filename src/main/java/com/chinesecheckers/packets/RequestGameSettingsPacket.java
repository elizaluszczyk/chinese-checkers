package com.chinesecheckers.packets;

import com.chinesecheckers.server.ServerPacket;

public class RequestGameSettingsPacket implements ServerPacket {
  private static final long serialVersionUID = 1L;
  private final String numberOfPlayersMessage;
  private final String gameTypeMessage;

  public RequestGameSettingsPacket(String numberOfPlayersMessage, String gameTypeMessage) {
    this.numberOfPlayersMessage = numberOfPlayersMessage;
    this.gameTypeMessage = gameTypeMessage;
  }

  public String getNumberOfPlayersMessage() {
    return numberOfPlayersMessage;
  }

  public String getGameTypeMessage() {
    return gameTypeMessage;
  }

  @Override
  public PacketType getType() {
    return PacketType.REQUEST_GAME_SETTINGS;
  }
}
