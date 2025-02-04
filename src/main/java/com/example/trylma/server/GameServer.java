package com.example.trylma.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import com.example.trylma.board.ChineseCheckersBoard;
import com.example.trylma.board.Move;
import com.example.trylma.game.BotPlayer;
import com.example.trylma.interfaces.GameManager;
import com.example.trylma.interfaces.Player;

public class GameServer {
    private final int port;
    private static int numberOfPlayers = 0;
    private static String gameType = null;
    protected static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    protected static final ArrayList<Player> players = new ArrayList<>();
    protected static final ArrayList<String> playersWhoWon = new ArrayList<>();
    protected static GameManager gameManager;
    protected static Integer currentPlayerIndex = null;
    private static BotPlayer botPlayer;

    public GameServer(int port) {
        this.port = port;
    }

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static ArrayList<String> getPlayersWhoWon() {
        return playersWhoWon;
    }

    public static void addWinner(String winner) {
        playersWhoWon.add(winner);
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

    public static String getGameType() {
        return gameType;
    }

    public static void setGameType(String gameType) {
        GameServer.gameType = gameType;
    }

    private static void notifyCurrentPlayer() {
        ClientHandler currentHandler = clientHandlers.get(currentPlayerIndex);
        System.out.println("Notifying player " + currentHandler.getPlayer().getUsername() + " that it's their turn.");
        currentHandler.transmitTurnUpdate("It's your turn!");
    }

    public static void setCurrentPlayerIndex(int index) {
        currentPlayerIndex = index;
        System.out.println("Current player index: " + currentPlayerIndex);
    }

    private static void incrementPlayerIndex() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        System.out.println("Next index " + currentPlayerIndex);
    }

    public static void moveToNextTurn() {
        Random random = new Random();
        int randomIndex = random.nextInt(players.size());

        if (GameServer.currentPlayerIndex == null) {
            GameServer.setCurrentPlayerIndex(randomIndex);
        } 
        
        Player currentPlayer = GameServer.players.get(GameServer.currentPlayerIndex);
        System.out.println("Current player: " + currentPlayer.getUsername());
        currentPlayer.setPlayerTurn(false);

        incrementPlayerIndex();

        Player nextPlayer = GameServer.players.get(GameServer.currentPlayerIndex);
        System.out.println("Next player: " + nextPlayer.getUsername());

        if (nextPlayer instanceof BotPlayer) {
            moveToBotTurn(botPlayer);
        } else {
            nextPlayer.setPlayerTurn(true);
            notifyCurrentPlayer();
        }
    }
    
    private static void moveToBotTurn(BotPlayer botPlayer) {
        System.out.println("It's bot turn");

        gameManager = GameManagerSingleton.getInstance();

        ChineseCheckersBoard updatedBoard = gameManager.getBoard();
        System.out.println("Updated board: " + updatedBoard);

        Move botMove = botPlayer.generateMove(updatedBoard);
        System.out.println("Bot move: " + botMove);
        gameManager.applyMoveForBot(botMove);

        broadcastBoardUpdate(updatedBoard, null);

        if (gameManager.isWinningMove(botPlayer)) {
            broadcastMessage("Bot player won!", null);
            addWinner(botPlayer.getUsername());
        }
        else {
            moveToNextTurn();
        }
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

    public static synchronized void broadcastBoardUpdate(ChineseCheckersBoard board, ClientHandler sender) {
        for (ClientHandler client : clientHandlers) {
            client.transmitBoardUpdate(board);
        }
    }

    public static ArrayList<Player> getAllPlayers() {
        return players;
    }

    public static BotPlayer getBotPlayer() {
        return botPlayer;
    }

    public static void setBotPlayer(BotPlayer botPlayer) {
        GameServer.botPlayer = botPlayer;
    }
}
