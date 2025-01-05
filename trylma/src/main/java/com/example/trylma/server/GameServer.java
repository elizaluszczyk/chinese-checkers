package com.example.trylma.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.example.trylma.board.Move;
import com.example.trylma.game.GamePlayer;
import com.example.trylma.game.StandardGameManager;
import com.example.trylma.interfaces.GameManager;
import com.example.trylma.packets.TextMessagePacket;
;

public class GameServer {
    private final int port;
    private static int numberOfPlayers = 0;
    protected static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    protected static GameManager gameManager;

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
                String username = waitForTextMessage(clientHandler);
                clientHandler.setPlayer(new GamePlayer(username));

                System.out.println("Client connected: " + username);

                if (clientHandlers.size() == 1) {
                    // ask about game type first if different number of players
                    clientHandler.transmitMessage("Enter the number of players (2,3,4 or 6):");
                    numberOfPlayers = Integer.parseInt(waitForTextMessage(clientHandler));
                    if (numberOfPlayers < 2 || numberOfPlayers == 5 || numberOfPlayers > 6) {
                        clientHandler.transmitMessage("Invalid number of players. Restart the server.");
                        break;
                    }
                    clientHandler.transmitMessage("Number of players set to: " + numberOfPlayers);

                    clientHandler.transmitMessage("Choose game variant (default):");
                    String gameType = waitForTextMessage(clientHandler);
                    initializeGame(gameType);
                    clientHandler.transmitMessage("You selected the game variant: " + gameType);
                } else {
                    clientHandler.transmitMessage("The game variant has already been chosen. Joining the game...");
                    
                }

                new Thread(clientHandler).start();

                if (clientHandlers.size() == numberOfPlayers) {
                    System.out.println("All players are in the game!");
                    broadcastMessage("The game is starting!", null);
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private void initializeGame(String gameType) { 
        gameManager = new StandardGameManager(gameType, numberOfPlayers);
    }

    private String waitForTextMessage(ClientHandler clientHandler) {
        try {
            TextMessagePacket packet = clientHandler.receiveTextMessage();
            return packet.getMessageString();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error receiving text message: " + e.getMessage());
        }
        return null;
    }

    public static synchronized void broadcastMessage(String message, ClientHandler sender) {
        String formattedMessage = (sender != null && sender.getPlayer() != null)
        ? sender.getPlayer().getUsername() + ": " + message
        : message;

        for (ClientHandler client : clientHandlers) {
            if (client != sender) { 
                client.transmitMessage(formattedMessage);
            }
        }
    }

    public static synchronized void broadcastMoveAndBoardUpdate(Move move, ClientHandler sender) {
        for (ClientHandler client : clientHandlers) {
                client.transmitMove(move);
                client.transmitBoardUpdate(gameManager.getBoard());
        }
    }

    public static synchronized void broadcastInvalidMove(Move invalidMove, ClientHandler sender) {
        for (ClientHandler client : clientHandlers) {
                client.transmitInvalidMove(invalidMove);
        }
    }
}