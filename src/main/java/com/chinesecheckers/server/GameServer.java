package com.chinesecheckers.server;

import com.chinesecheckers.board.ChineseCheckersBoard;
import com.chinesecheckers.board.Move;
import com.chinesecheckers.exceptions.UnknownPacketException;
import com.chinesecheckers.game.BotPlayer;
import com.chinesecheckers.interfaces.GameManager;
import com.chinesecheckers.interfaces.Player;
import com.chinesecheckers.packets.PacketType;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameServer {
  private static final Logger logger = LoggerFactory.getLogger(GameServer.class);

  // server configuration
  private final int port;

  // game state variables
  private static int numberOfPlayers = 0;
  private static String gameType = null;
  private static Integer currentPlayerIndex = null;
  private static BotPlayer botPlayer;

  protected static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
  protected static final ArrayList<Player> players = new ArrayList<>();
  protected static final ArrayList<String> playersWhoWon = new ArrayList<>();

  protected static GameManager gameManager;

  public GameServer(int port) {
    this.port = port;
  }

  // server methods
  public void start() {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      logger.info("Server running on port {}", port);

      while (true) {
        logger.info("Waiting for a new player...");
        Socket clientSocket = serverSocket.accept();

        ClientHandler clientHandler = new ClientHandler(clientSocket);
        new Thread(clientHandler).start();
      }
    } catch (IOException e) {
      logger.error("Server error: {}", e.getMessage(), e);
    }
  }

  // player management methods
  public static void addPlayer(Player player) {
    players.add(player);
  }

  public static ArrayList<Player> getAllPlayers() {
    return players;
  }

  public static ArrayList<String> getPlayersWhoWon() {
    return playersWhoWon;
  }

  public static void addWinner(String winner) {
    playersWhoWon.add(winner);
  }

  // game configuration methods
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

  public static BotPlayer getBotPlayer() {
    return botPlayer;
  }

  public static void setBotPlayer(BotPlayer botPlayer) {
    GameServer.botPlayer = botPlayer;
  }

  // turn management methods
  public static void setCurrentPlayerIndex(int index) {
    currentPlayerIndex = index;
    logger.debug("Current player index: {}", currentPlayerIndex);
  }

  private static void notifyCurrentPlayer() throws UnknownPacketException {
    ClientHandler currentHandler = clientHandlers.get(currentPlayerIndex);
    logger.info(
        "Notifying player {} that it's their turn.", currentHandler.getPlayer().getUsername());
    currentHandler.transmit(PacketType.TURN_UPDATE, "It's your turn!");
  }

  private static void incrementPlayerIndex() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    logger.debug("Next index {}", currentPlayerIndex);
  }

  public static void moveToNextTurn() throws UnknownPacketException {
    Random random = new Random();
    int randomIndex = random.nextInt(players.size());

    if (GameServer.currentPlayerIndex == null) {
      GameServer.setCurrentPlayerIndex(randomIndex);
    }

    for (Player p : players) {
      p.setPlayerTurn(false);
    }

    incrementPlayerIndex();

    Player nextPlayer = GameServer.players.get(GameServer.currentPlayerIndex);
    logger.info("Next player: {}", nextPlayer.getUsername());

    if (nextPlayer instanceof BotPlayer) {
      moveToBotTurn(botPlayer);
    } else {
      nextPlayer.setPlayerTurn(true);
      notifyCurrentPlayer();
    }
  }

  private static void moveToBotTurn(BotPlayer botPlayer) throws UnknownPacketException {
    logger.info("It's bot turn");

    gameManager = GameManagerSingleton.getInstance();

    ChineseCheckersBoard updatedBoard = gameManager.getBoard();
    logger.debug("Updated board: {}", updatedBoard);

    Move botMove = botPlayer.generateMove(updatedBoard);
    logger.info("Bot move: {}", botMove);
    gameManager.applyMoveForBot(botMove);

    broadcastBoardUpdate(updatedBoard, null);

    if (gameManager.isWinningMove(botPlayer)) {
      broadcastMessage("Bot player won!", null);
      addWinner(botPlayer.getUsername());
    } else {
      moveToNextTurn();
    }
  }

  // communication methods
  public static synchronized void broadcastMessage(String message, ClientHandler sender)
      throws UnknownPacketException {
    String formattedMessage =
        (sender != null && sender.getPlayer() != null)
            ? sender.getPlayer().getUsername() + ": " + message
            : message;

    for (ClientHandler client : clientHandlers) {
      if (client != sender) {
        client.transmit(PacketType.TEXT_MESSAGE, formattedMessage);
      }
    }
  }

  public static synchronized void broadcastBoardUpdate(
      ChineseCheckersBoard board, ClientHandler sender) throws UnknownPacketException {
    for (ClientHandler client : clientHandlers) {
      client.transmit(PacketType.BOARD_UPDATE, board);
    }
  }
}
