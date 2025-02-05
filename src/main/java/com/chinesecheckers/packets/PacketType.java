package com.chinesecheckers.packets;

public enum PacketType {
    TEXT_MESSAGE,
    MOVE,
    USERNAME,
    GAME_SETTINGS,
    TURN_SKIP,
    BOARD_UPDATE,
    INVALID_MOVE,
    REQUEST_USERNAME,
    REQUEST_GAME_SETTINGS,
    TURN_UPDATE,
    INVALID_GAME_SETTINGS,
    WIN
}
