package com.chinesecheckers.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinesecheckers.board.ChineseCheckersBoard;
import com.chinesecheckers.board.Move;
import com.chinesecheckers.game.BotPlayer;
import com.chinesecheckers.game.GamePlayer;
import com.chinesecheckers.game.GameType;
import com.chinesecheckers.game.StandardGameManager;
import com.chinesecheckers.interfaces.GameManager;
import com.chinesecheckers.interfaces.Player;
import com.chinesecheckers.packets.BoardUpdatePacket;
import com.chinesecheckers.packets.GameSettingsPacket;
import com.chinesecheckers.packets.InvalidGameSettingsPacket;
import com.chinesecheckers.packets.InvalidMovePacket;
import com.chinesecheckers.packets.MovePacket;
import com.chinesecheckers.packets.RequestGameSettingsPacket;
import com.chinesecheckers.packets.RequestUsernamePacket;
import com.chinesecheckers.packets.TextMessagePacket;
import com.chinesecheckers.packets.TurnSkipPacket;
import com.chinesecheckers.packets.TurnUpdatePacket;
import com.chinesecheckers.packets.UsernamePacket;
import com.chinesecheckers.packets.WinPacket;

public class ClientHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    // constants for game types
    private static final String GAME_TYPE_DEFAULT = "default";
    private static final String GAME_TYPE_DEFAULT_WITH_BOT = "defaultWithBot";
    private static final String GAME_TYPE_YIN_YANG = "YinAndYang";
    
    private final Socket clientSocket;
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;

    private Player player;
    private GameManager gameManager;
    private boolean playerTurn = true;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    // core lifecycle method
    @Override
    public void run() {
        try {
            setupPlayer();
            
            synchronized (GameServer.class) {
                GameServer.clientHandlers.add(this);
                GameServer.players.add(this.getPlayer());
                setupGameSettings(GameServer.clientHandlers.size());

                if (GameServer.getGameType().equals(GAME_TYPE_DEFAULT_WITH_BOT)) {
                    handleDefaultWithBotGameStart();
                } else {
                    handleStandardGameStart();
                }
            }

            while (true) {
                ServerPacket serverPacket = (ServerPacket) objectInputStream.readObject();
                logger.debug("Received packet: {}", serverPacket.getClass().getName());
                handlePacket(serverPacket);
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Connection lost with client: {}", player != null ? player.getUsername() : "unknown", e);
        } finally {
            cleanup();
        }
    }
    
    // player and game setup methods
    private void setupPlayer() throws ClassNotFoundException, IOException {
        transmitRequestUsername("Enter your username:");
        ServerPacket serverPacket = (ServerPacket) objectInputStream.readObject();
        handlePacket(serverPacket);
    }

    private void setupGameSettings(int currentPlayers) throws ClassNotFoundException, IOException {
        if (currentPlayers == 1) {
            transmitRequestGameSettings("Enter the number of players (2, 3, 4, or 6)", "Enter the game variant (default)");
            ServerPacket serverPacket = (ServerPacket) objectInputStream.readObject();
            handlePacket(serverPacket);
        } else {
            transmitMessage("Game settings already configured. Joining the game...");
        }
    }
    
    private void handleDefaultWithBotGameStart() {
        if (GameServer.clientHandlers.size() == GameServer.getNumberOfPlayers()) {
            BotPlayer botPlayer = new BotPlayer("botPlayer");
            GameServer.setBotPlayer(botPlayer);
            GameServer.players.add(botPlayer);
            logger.info("Bot player added to the game");

            initializeGameManager();
            
            logger.debug("Players: {}", GameServer.players);
        }
    }
    
    private void handleStandardGameStart() {
        if (GameServer.players.size() == GameServer.getNumberOfPlayers()) {
            initializeGameManager();
        }
    }
    
    private void initializeGameManager() {
        GameManagerSingleton.setInstance(new StandardGameManager(GameServer.getGameType(), GameServer.getNumberOfPlayers()));
        GameServer.broadcastMessage("The game is starting!", null);

        GameServer.moveToNextTurn();

        gameManager = GameManagerSingleton.getInstance();
        GameServer.broadcastBoardUpdate(gameManager.getBoard(), this);
    }

    // validation methods
    private boolean validateNumberOfPlayers(String gameType, int numberOfPlayers) {
        boolean isValid = switch (gameType) {
            case GAME_TYPE_DEFAULT -> numberOfPlayers >= 2 && numberOfPlayers != 5 && numberOfPlayers <= 6;
            case GAME_TYPE_DEFAULT_WITH_BOT -> numberOfPlayers >= 1 && numberOfPlayers != 4 && numberOfPlayers <= 5;
            case GAME_TYPE_YIN_YANG -> numberOfPlayers == 2;
            default -> false; 
        };
    
        if (!isValid) {
            transmitInvalidGameSettings("Invalid number of players, enter settings again");
        }
    
        return isValid;
    }

    private boolean validateGameType(String gameType) {
        if (GameType.fromString(gameType) != null) {
            return true;
        } else {
            transmitInvalidGameSettings("Invalid game type, enter settings again");
            return false;
        }
    }

    // packet transmission methods
    public void transmitMessage(String message) {
        transmitPacket(new TextMessagePacket(message));
    }

    public void transmitMove(Move move) {
        transmitPacket(new MovePacket(move));
    }

    public void transmitBoardUpdate(ChineseCheckersBoard board) {
        transmitPacket(new BoardUpdatePacket(board.getBoard()));
    }

    public void transmitInvalidMove(Move invalidMove) {
        transmitPacket(new InvalidMovePacket(invalidMove));
    }

    public void transmitRequestUsername(String message) {
        transmitPacket(new RequestUsernamePacket(message));
    }

    public void transmitRequestGameSettings(String numberOfPlayersMessage, String gameTypeMessage) {
        transmitPacket(new RequestGameSettingsPacket(numberOfPlayersMessage, gameTypeMessage));
    }

    public void transmitTurnUpdate(String message) {
        transmitPacket(new TurnUpdatePacket(message));
    }

    public void transmitInvalidGameSettings(String message) {
        transmitPacket(new InvalidGameSettingsPacket(message));
    }

    public void transmitWin(String message) {
        transmitPacket(new WinPacket(message));
    }

    private void transmitPacket(ServerPacket packet) {
        try {
            logger.debug("Sending packet: {}", packet.getClass().getName());
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();
        } catch (IOException e) {
            logger.error("Failed to transmit packet", e);
        }
    }

    // packet handling methods
    private void handlePacket(ServerPacket packet) {
        if (packet instanceof TextMessagePacket textMessagePacket) {
            handleTextMessage(textMessagePacket);
        } else if (packet instanceof MovePacket movePacket) {
            handleMove(movePacket);
        } else if (packet instanceof UsernamePacket usernamePacket) {
            handleUsername(usernamePacket);
        } else if (packet instanceof GameSettingsPacket gameSettingsPacket) {
            handleGameSettings(gameSettingsPacket);
        } else if (packet instanceof TurnSkipPacket) {
            handleTurnSkipPacket();
        } else {
            logger.warn("Unknown packet type received: {}", packet.getClass().getName());
        }
    }

    private void handleTextMessage(TextMessagePacket packet) {
        String message = packet.getMessageString();
        logger.info("Received text message: {}", message);
        GameServer.broadcastMessage(message, this);
    }

    private void handleMove(MovePacket packet) {
        Move move = packet.getMove();
        logger.info("Received move: {}", move);

        gameManager = GameManagerSingleton.getInstance();

        if (!isPlayerTurn()) {
            transmitInvalidMove(move);
            return;
        }

        if (GameServer.getPlayersWhoWon().contains(this.getPlayer().getUsername())) {
            return;
        }
        
        if (gameManager.isMoveValid(move, this.getPlayer())) {
            processValidMove(move);
        } else {
            transmitInvalidMove(move);
            transmitTurnUpdate("Move was invalid. Try again, it's your turn!");
        }
    }
    
    private void processValidMove(Move move) {
        gameManager.applyMove(move);

        ChineseCheckersBoard updatedBoard = gameManager.getBoard();
        GameServer.broadcastBoardUpdate(updatedBoard, this);

        if (gameManager.isWinningMove(this.getPlayer())) {
            transmitWin("You win! End of the game");
            GameServer.broadcastMessage("Player " + this.getPlayer().getUsername() + " won! End of the game", this);
            GameServer.addWinner(this.getPlayer().getUsername());
        }

        GameServer.moveToNextTurn();
    }

    private void handleUsername(UsernamePacket packet) {
        String username = packet.getUsername();
        logger.info("Received username: {}", username);

        this.player = new GamePlayer(username);
        logger.info("Client connected: {}", username);
    }

    private void handleGameSettings(GameSettingsPacket packet) {
        int numberOfPlayers = packet.getNumberOfPlayers();
        logger.info("Received number of players: {}", numberOfPlayers);
        String gameType = packet.getGameType();
        logger.info("Received game type: {}", gameType);

        validateGameType(gameType);
        GameServer.setGameType(gameType);

        validateNumberOfPlayers(gameType, numberOfPlayers);
        GameServer.setNumberOfPlayers(numberOfPlayers);
        transmitMessage("Game settings applied: " + numberOfPlayers + " players, variant: " + gameType);
    }

    private void handleTurnSkipPacket() {
        GameServer.moveToNextTurn();
    }

    // cleanup method
    private void cleanup() {
        synchronized (GameServer.class) {
            GameServer.clientHandlers.remove(this);
            GameServer.players.remove(this.getPlayer());
            if (player != null) {
                GameServer.broadcastMessage(player.getUsername() + " has left the game.", null);
            }
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            logger.error("Failed to close client socket: {}", player != null ? player.getUsername() : "unknown", e);
        }
    }
}
