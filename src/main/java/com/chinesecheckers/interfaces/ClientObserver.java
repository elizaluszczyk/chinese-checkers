package com.example.trylma.interfaces;

import com.example.trylma.server.ServerPacket;

public interface ClientObserver {
    void notifyPacket(ServerPacket serverPacket);
}
