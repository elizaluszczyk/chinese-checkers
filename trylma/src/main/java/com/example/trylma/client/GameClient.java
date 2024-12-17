package com.example.trylma.client;

import com.example.chinese_checkers.ServerPacket;
import com.example.chinese_checkers.packet.TextMessagePacket;

import java.io.*;
import java.net.Socket;

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
                        ServerPacket serverPacket = (ServerPacket)objectInputStream.readObject();
                        System.out.println(serverPacket);
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
        System.out.println("Received board update: " + packet.getBoard());

        Board board = packet.getBoard();

        for (int row_index = 0; row_index < board.getBoard().size(); row_index++) {
            for (int column_index = 0; column_index < board.getBoard().get(row_index).size(); column_index++) {
                if (!board.isInBoard(row_index, column_index)) continue;

                if (board.getBoard().get(row_index).get(column_index) == null)
                {
                    System.out.print("0 ");
                    continue;
                }

                System.out.print("x ");
            }

            System.out.print("\n");
        }
    }
}
