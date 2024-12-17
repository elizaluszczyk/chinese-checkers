package com.example.trylma.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.example.trylma.game.GamePlayer;
import com.example.trylma.packets.TextMessagePacket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final BufferedReader reader;
    private final ObjectOutputStream objectOutputStream;
    private GamePlayer player;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    public void setPlayer(GamePlayer player) {
        this.player = player;
    }

    public GamePlayer getPlayer() {
        return player;
    }

    public String receiveMessage() throws IOException {
        return reader.readLine();
    }

    public void transmitMessage(String message) {
        transmitPacket(new TextMessagePacket(message));
    }

    public void transmitPacket(ServerPacket packet) {
        try {
           objectOutputStream.writeObject(packet);
           objectOutputStream.flush();
        } catch (IOException e) {
            System.err.println("Failed to transmit packet:" + e.getMessage());
        } 
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = reader.readLine();
                if (message == null) {
                    break;
                }
                GameServer.broadcastMessage(message, this);
            }
        } catch (IOException e) {
            System.err.println("Connection lost with client: " + e.getMessage());
        } finally {
            GameServer.clientHandlers.remove(this);
            if (player != null) {
                GameServer.broadcastMessage(player.getUsername() + " has left the game.", null);
            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Failed to close client socket: " + e.getMessage());
            }
        }
    }
}
