package com.chinesecheckers.interfaces;

import com.chinesecheckers.server.ServerPacket;

public interface ClientObserver {
    void notifyPacket(ServerPacket serverPacket);
}
