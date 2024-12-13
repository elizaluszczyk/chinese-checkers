package com.example.trylma.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.example.trylma.GamePlayer;
import com.example.trylma.ServerPacket;
import com.example.trylma.interfaces.Player;
import com.example.trylma.packets.TextMessagePacket;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private GamePlayer player;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public void transmitMessage(String message) {
        transmitPacket(new TextMessagePacket(message));
    }

    public void transmitPacket(ServerPacket packet) {
        try {
            // object serialization for JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPacket = objectMapper.writeValueAsString(packet);

            // sending packet 
            writer.println(jsonPacket);

        } catch (IOException e) {
            System.err.println("Failed to transmit packet:" + e.getMessage());
        } 
    }

    public String receiveMessage() throws IOException {
        return reader.readLine();
    }

    public void setPlayer(GamePlayer player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void run() {
        
    }
}
