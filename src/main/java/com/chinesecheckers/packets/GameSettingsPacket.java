package com.chinesecheckers.packets;

import com.chinesecheckers.server.ServerPacket;

public class GameSettingsPacket implements ServerPacket {
  private static final long serialVersionUID = 1L;
  private final int numberOfPlayers;
  private final String gameType;

  public GameSettingsPacket(int numberOfPlayers, String gameType) {
    this.numberOfPlayers = numberOfPlayers;
    this.gameType = gameType;
  }

  public int getNumberOfPlayers() {
    return numberOfPlayers;
  }

  public String getGameType() {
    return gameType;
  }

  @Override
  public PacketType getType() {
    return PacketType.GAME_SETTINGS;
  }
}
