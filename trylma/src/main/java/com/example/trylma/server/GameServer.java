package com.example.trylma.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.example.trylma.board.Move;
import com.example.trylma.interfaces.Board;
import com.example.trylma.interfaces.GameManager;

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

    public static synchronized void broadcastInvalidMove(Move invalidMove, ClientHandler sender) {
        for (ClientHandler client : clientHandlers) {
            client.transmitInvalidMove(invalidMove);
        }
    }
}
