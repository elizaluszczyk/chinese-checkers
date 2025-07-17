package com.chinesecheckers.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinesecheckers.board.ChineseCheckersBoard;
import com.chinesecheckers.board.Move;
import com.chinesecheckers.exceptions.UnknownPacketException;
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
import com.chinesecheckers.packets.PacketType;
import com.chinesecheckers.packets.RequestGameSettingsPacket;
import com.chinesecheckers.packets.RequestUsernamePacket;
import com.chinesecheckers.packets.TextMessagePacket;
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

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    public Player getPlayer() {
        return player;
    }

    // core lifecycle method
    @Override
    public void run() {
        try {
            setupPlayer();
            
            synchronized (GameServer.class) {
                GameServer.clientHandlers.add(this);
                GameServer.players.add(player);
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
        } catch (UnknownPacketException e) {
            logger.error("Unknown packet received from client: {}", player != null ? player.getUsername() : "unknown", e);
        } finally {
            try {
                cleanup();
            } catch (UnknownPacketException e) {
                logger.error("Unknown packet received from client: {}", player != null ? player.getUsername() : "unknown", e);
            }
        }
    }
    
    // player and game setup methods
    private void setupPlayer() throws ClassNotFoundException, IOException, UnknownPacketException {
        transmit(PacketType.REQUEST_USERNAME, "Enter your username:");
        ServerPacket serverPacket = (ServerPacket) objectInputStream.readObject();
        handlePacket(serverPacket);
    }

    private void setupGameSettings(int currentPlayers) throws ClassNotFoundException, IOException, UnknownPacketException {
        if (currentPlayers == 1) {
            transmit(PacketType.REQUEST_GAME_SETTINGS, "Enter the number of players (2, 3, 4, or 6)", "Enter the game variant (default)");
            ServerPacket serverPacket = (ServerPacket) objectInputStream.readObject();
            handlePacket(serverPacket);
        } else {
            transmit(PacketType.TEXT_MESSAGE, "Game settings already configured. Joining the game...");
        }
    }
    
    private void handleDefaultWithBotGameStart() throws UnknownPacketException {
        if (GameServer.clientHandlers.size() == GameServer.getNumberOfPlayers()) {
            BotPlayer botPlayer = new BotPlayer("botPlayer");
            GameServer.setBotPlayer(botPlayer);
            GameServer.players.add(botPlayer);
            logger.info("Bot player added to the game");

            initializeGameManager();
            
            logger.debug("Players: {}", GameServer.players);
        }
    }
    
    private void handleStandardGameStart() throws UnknownPacketException {
        if (GameServer.players.size() == GameServer.getNumberOfPlayers()) {
            initializeGameManager();
        }
    }
    
    private void initializeGameManager() throws UnknownPacketException {
        GameManagerSingleton.setInstance(new StandardGameManager(GameServer.getGameType(), GameServer.getNumberOfPlayers()));
        GameServer.broadcastMessage("The game is starting!", null);

        GameServer.moveToNextTurn();

        gameManager = GameManagerSingleton.getInstance();
        GameServer.broadcastBoardUpdate(gameManager.getBoard(), this);
    }

    // validation methods
    private boolean validateNumberOfPlayers(String gameType, int numberOfPlayers) throws UnknownPacketException {
        boolean isValid = switch (gameType) {
            case GAME_TYPE_DEFAULT -> numberOfPlayers >= 2 && numberOfPlayers != 5 && numberOfPlayers <= 6;
            case GAME_TYPE_DEFAULT_WITH_BOT -> numberOfPlayers >= 1 && numberOfPlayers != 4 && numberOfPlayers <= 5;
            case GAME_TYPE_YIN_YANG -> numberOfPlayers == 2;
            default -> false; 
        };
    
        if (!isValid) {
            transmit(PacketType.INVALID_GAME_SETTINGS, "Invalid number of players, enter settings again");
        }
    
        return isValid;
    }

    private boolean validateGameType(String gameType) throws UnknownPacketException {
        if (GameType.fromString(gameType) != null) {
            return true;
        } else {
            transmit(PacketType.INVALID_GAME_SETTINGS, "Invalid game type, enter settings again");
            return false;
        }
    }

    // packet transmission methods
    private void transmitPacket(ServerPacket packet) {
        try {
            logger.debug("Sending packet: {}", packet.getClass().getName());
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();
        } catch (IOException e) {
            logger.error("Failed to transmit packet", e);
        }
    }

    public void transmit(PacketType type, Object... args) throws UnknownPacketException {
        ServerPacket packet = switch (type) {
            case TEXT_MESSAGE -> new TextMessagePacket((String) args[0]);
            case MOVE -> new MovePacket((Move) args[0]);
            case BOARD_UPDATE -> new BoardUpdatePacket(((ChineseCheckersBoard) args[0]).getBoard());
            case INVALID_MOVE -> new InvalidMovePacket((Move) args[0]);
            case REQUEST_USERNAME -> new RequestUsernamePacket((String) args[0]);
            case REQUEST_GAME_SETTINGS -> new RequestGameSettingsPacket((String) args[0], (String) args[1]);
            case TURN_UPDATE -> new TurnUpdatePacket((String) args[0]);
            case INVALID_GAME_SETTINGS -> new InvalidGameSettingsPacket((String) args[0]);
            case WIN -> new WinPacket((String) args[0]);
            case TURN_SKIP, USERNAME, GAME_SETTINGS -> throw new UnknownPacketException("Cannot transmit server-to-client packet type: " + type);
            default -> throw new UnknownPacketException("Unknown packet type: " + type);
        };
        transmitPacket(packet);
    }

    // packet handling methods
    private void handlePacket(ServerPacket packet) throws UnknownPacketException {
        switch (packet.getType()) {
            case TEXT_MESSAGE -> handleTextMessage((TextMessagePacket) packet);
            case MOVE -> handleMove((MovePacket) packet);
            case USERNAME -> handleUsername((UsernamePacket) packet);
            case GAME_SETTINGS -> handleGameSettings((GameSettingsPacket) packet);
            case TURN_SKIP -> handleTurnSkipPacket();
            case BOARD_UPDATE, INVALID_MOVE, REQUEST_USERNAME, REQUEST_GAME_SETTINGS,
                 TURN_UPDATE, INVALID_GAME_SETTINGS, WIN -> throw new UnknownPacketException("Unexpected client-to-server packet received: " + packet.getType());
            default -> throw new UnknownPacketException("Unknown packet type: " + packet.getType());
        }
    }

    private void handleTextMessage(TextMessagePacket packet) throws UnknownPacketException {
        String message = packet.getMessage();
        logger.info("Received text message: {}", message);
        GameServer.broadcastMessage(message, this);
    }

    private void handleMove(MovePacket packet) throws UnknownPacketException {
        Move move = packet.getMove();
        logger.info("Received move: {}", move);

        gameManager = GameManagerSingleton.getInstance();

        if (!player.isPlayerTurn()) {
            transmit(PacketType.MOVE, move);
            return;
        }

        if (GameServer.getPlayersWhoWon().contains(player.getUsername())) {
            return;
        }
        
        if (gameManager.isMoveValid(move, player)) {
            processValidMove(move);
        } else {
            transmit(PacketType.INVALID_MOVE, move);
            transmit(PacketType.TURN_UPDATE, "Move was invalid. Try again, it's your turn!");
        }
    }
    
    private void processValidMove(Move move) throws UnknownPacketException {
        gameManager.applyMove(move);

        ChineseCheckersBoard updatedBoard = gameManager.getBoard();
        GameServer.broadcastBoardUpdate(updatedBoard, this);

        if (gameManager.isWinningMove(player)) {
            transmit(PacketType.WIN, "You win! End of the game");
            GameServer.broadcastMessage("Player " + player.getUsername() + " won! End of the game", this);
            GameServer.addWinner(player.getUsername());
        }

        GameServer.moveToNextTurn();
    }

    private void handleUsername(UsernamePacket packet) {
        String username = packet.getUsername();
        logger.info("Received username: {}", username);

        this.player = new GamePlayer(username);
        logger.info("Client connected: {}", username);
    }

    private void handleGameSettings(GameSettingsPacket packet) throws UnknownPacketException {
        int numberOfPlayers = packet.getNumberOfPlayers();
        logger.info("Received number of players: {}", numberOfPlayers);
        String gameType = packet.getGameType();
        logger.info("Received game type: {}", gameType);

        validateGameType(gameType);
        GameServer.setGameType(gameType);

        validateNumberOfPlayers(gameType, numberOfPlayers);
        GameServer.setNumberOfPlayers(numberOfPlayers);
        transmit(PacketType.TEXT_MESSAGE, "Game settings applied: " + numberOfPlayers + " players, variant: " + gameType);
    }

    private void handleTurnSkipPacket() throws UnknownPacketException {
        GameServer.moveToNextTurn();
    }

    // cleanup method
    private void cleanup() throws UnknownPacketException {
        synchronized (GameServer.class) {
            GameServer.clientHandlers.remove(this);
            GameServer.players.remove(player);
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
