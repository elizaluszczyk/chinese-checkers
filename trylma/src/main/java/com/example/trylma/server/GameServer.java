package com.example.trylma.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import com.example.trylma.interfaces.Board;
import com.example.trylma.interfaces.GameManager;

public class GameServer {
    private final int port;
    private static int numberOfPlayers = 0;
    protected static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    protected static GameManager gameManager;
    protected static Integer currentPlayerIndex = null;

    public GameServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running on port " + port);

            while (true) {
                System.out.println("Waiting for a new player...");
                Socket clientSocket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public static void setNumberOfPlayers(int numberOfPlayers) {
        GameServer.numberOfPlayers = numberOfPlayers;
    }

    public static int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public static void setCurrentPlayerIndex(int index) {
        currentPlayerIndex = index;
    }

    public static void incrementPlayerIndex() {
        currentPlayerIndex = (currentPlayerIndex + 1) % clientHandlers.size();
    }

    public static void notifyCurrentPlayer() {
        ClientHandler currentHandler = GameServer.clientHandlers.get(GameServer.currentPlayerIndex);
        System.out.println("Notifying player " + currentHandler.getPlayer().getUsername() + " that it's their turn.");
        currentHandler.transmitTurnUpdate("It's your turn! You can move");
    }

    public static void moveToNextTurn() {
        Random random = new Random();
        int randomIndex = random.nextInt(clientHandlers.size());

        if (GameServer.currentPlayerIndex == null) {
            GameServer.setCurrentPlayerIndex(randomIndex);
        } 
        
        ClientHandler currentHandler = GameServer.clientHandlers.get(GameServer.currentPlayerIndex);
        currentHandler.setPlayerTurn(false);

        GameServer.incrementPlayerIndex();
    
        ClientHandler nextHandler = GameServer.clientHandlers.get(GameServer.currentPlayerIndex);
        nextHandler.setPlayerTurn(true);
    
        notifyCurrentPlayer();
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

    public static synchronized void broadcastBoardUpdate(Board board, ClientHandler sender) {
        for (ClientHandler client : clientHandlers) {
            client.transmitBoardUpdate(board);
        }
    }
}
