package com.example.trylma.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.example.trylma.GamePlayer;

public class GameServer {
    private final int port;
    private static int numberOfPlayers = 0;
    protected static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public GameServer(int port) {
        this.port = port;
    }
    
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running on port " + port);

            while (true) { 
                System.out.println("Waiting for a new player...");
                Socket clientSocket = serverSocket.accept();

                ClientHandler clientHandler =  new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);

                clientHandler.transmitMessage("Enter your username:");
                String username = clientHandler.receiveMessage();
                clientHandler.setPlayer(new GamePlayer(username));

                System.out.println("Client connected: " + username);

                if (clientHandlers.size() == 1) {
                    // ask about game type first if different number of players
                    clientHandler.transmitMessage("Enter the number of players (2,3,4 or 6):");
                    numberOfPlayers = Integer.parseInt(clientHandler.receiveMessage());
                    if (numberOfPlayers < 2 || numberOfPlayers == 5 || numberOfPlayers > 6) {
                        clientHandler.transmitMessage("Invalid number of players. Restart the server.");
                        break;
                    }
                    clientHandler.transmitMessage("Number of players set to: " + numberOfPlayers);
                }

                new Thread(clientHandler).start();

                if (clientHandlers.size() == numberOfPlayers) {
                    System.out.println("All players are in the game!");
                    // add a method to send messages between players
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
