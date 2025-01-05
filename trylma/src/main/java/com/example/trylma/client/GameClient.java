package com.example.trylma.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.example.trylma.board.Move;
import com.example.trylma.exceptions.InvalidMoveException;
import com.example.trylma.interfaces.Board;
import com.example.trylma.packets.BoardUpdatePacket;
import com.example.trylma.packets.MovePacket;
import com.example.trylma.packets.TextMessagePacket;
import com.example.trylma.parsers.StandardMoveParser;
import com.example.trylma.server.ServerPacket;

public class GameClient {
    private final String serverAddress;
    private final int port;
    private final StandardMoveParser moveParser = new StandardMoveParser();

    public GameClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(serverAddress, port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
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
            
            while (true) { 
                String input = consoleReader.readLine();
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Disconnecting...");
                    break;
                }

                try {
                    Object parsedInput = parseInput(input);
                    if (parsedInput instanceof Move move) {
                        MovePacket movePacket = new MovePacket(move);
                        objectOutputStream.writeObject(movePacket);  
                    } else if (parsedInput instanceof String message) {
                        TextMessagePacket textMessagePacket = new TextMessagePacket(message);
                        objectOutputStream.writeObject(textMessagePacket);
                    }

                    objectOutputStream.flush();
                } catch (InvalidMoveException e) {
                    System.err.println("Invalid input: " + e.getMessage());
                }
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
        } else if (packet instanceof MovePacket movePacket) {
            handleMove(movePacket);
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

    private void handleMove(MovePacket packet) {
        Move move = packet.getMove();
        System.out.println("Received move: " + move);
    }

    private Object parseInput(String input) throws InvalidMoveException {
        if (input.startsWith("MOVE")) {
            return moveParser.parseMove(input);
        }

        return input;
    }
}