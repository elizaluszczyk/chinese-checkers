package com.example.trylma.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.example.trylma.GamePlayer;
import com.example.trylma.exceptions.InvalidMoveException;
import com.example.trylma.game.Move;
import com.example.trylma.game.StandardGameManager;
import com.example.trylma.interfaces.GameManager;
import com.example.trylma.interfaces.MoveParser;
import com.example.trylma.interfaces.Player;
import com.example.trylma.packets.BoardUpdatePacket;
import com.example.trylma.parsers.StandardMoveParser;;

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

                    clientHandler.transmitMessage("Choose game variant (default):");
                    String gameType = clientHandler.receiveMessage();
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

    public static synchronized void broadcastMessage(String message, ClientHandler sender) {
        String formattedMessage = (sender != null && sender.getPlayer() != null)
        ? sender.getPlayer().getUsername() + ": " + message
        : message;

        // MOVE x1,y1 TO x2,y2
        if (message.startsWith("MOVE")) {
            MoveParser parser = new StandardMoveParser();
            try {
               Move move = parser.parseMove(message);
                
                if (gameManager != null) {
                    if (sender != null) {
                        Player currentPlayer = sender.getPlayer();
                        gameManager.getBoard().addMoveTakenByPlayer(currentPlayer, move);
                    }
                }

                for (ClientHandler client : clientHandlers) {
                    client.transmitPacket(new BoardUpdatePacket(gameManager.getBoard()));
                }
                
            } catch (InvalidMoveException e) {
                System.err.println("Error processing move: " + e.getMessage());
                if (sender != null) {
                    sender.transmitMessage("Invalid move format. Please try again.");
            }
            }
        }

        for (ClientHandler client : clientHandlers) {
            if (client != sender) { 
                client.transmitMessage(formattedMessage);
            }
        }


    }
}
