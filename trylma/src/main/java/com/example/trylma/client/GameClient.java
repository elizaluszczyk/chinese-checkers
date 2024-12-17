package com.example.trylma.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.example.trylma.interfaces.Board;
import com.example.trylma.packets.BoardUpdatePacket;
import com.example.trylma.packets.TextMessagePacket;
import com.example.trylma.server.ServerPacket;

public class GameClient {
    private final String serverAddress;
    private final int port;

    public GameClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(serverAddress, port);
             ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server: " + serverAddress + ":" + port);

            Thread receiveThread = new Thread(() -> {
               try {
                   while (true) { 
                       ServerPacket serverPacket = (ServerPacket) objectInputStream.readObject(); 
                       handlePacket(serverPacket);
                   }
               } catch (IOException | ClassNotFoundException e) {
                System.err.println("Connection to server lost: " + e.getMessage());
               }
            });
            receiveThread.start();

            System.out.println("You can send messages now (type 'exit' to quit):");
            String message;
            while ((message = consoleReader.readLine()) != null) {
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Disconnected from server.");
                    break;
                }
                writer.println(message);
            }

        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }

    private void handlePacket(ServerPacket packet) {
        if (packet instanceof TextMessagePacket textMessagePacket) {
            handleTextMessage(textMessagePacket);
        } else if (packet instanceof BoardUpdatePacket boardUpdatePacket) {
            handleBoardUpdate(boardUpdatePacket);
        } else {
            System.err.println("Unknown packet type received.");
        }
    }

    private void handleTextMessage(TextMessagePacket packet) {
        String message = packet.getMessageString();
        System.out.println("Received text message: " + message);
    }

    private void handleBoardUpdate(BoardUpdatePacket packet) {
        Board board = packet.getBoard();
        System.out.println("Received board update: " + board);

        for (String move : packet.getMovedPerformedByPlayers()) {
            System.out.println("Moves: " + move);
        }
    }
}