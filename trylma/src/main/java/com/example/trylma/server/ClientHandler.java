package com.example.trylma.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.example.trylma.board.ChineseCheckersBoard;
import com.example.trylma.board.Move;
import com.example.trylma.game.BotPlayer;
import com.example.trylma.game.GamePlayer;
import com.example.trylma.game.GameType;
import com.example.trylma.game.StandardGameManager;
import com.example.trylma.interfaces.GameManager;
import com.example.trylma.interfaces.Player;
import com.example.trylma.packets.BoardUpdatePacket;
import com.example.trylma.packets.GameSettingsPacket;
import com.example.trylma.packets.InvalidGameSettingsPacket;
import com.example.trylma.packets.InvalidMovePacket;
import com.example.trylma.packets.MovePacket;
import com.example.trylma.packets.RequestGameSettingsPacket;
import com.example.trylma.packets.RequestUsernamePacket;
import com.example.trylma.packets.TextMessagePacket;
import com.example.trylma.packets.TurnSkipPacket;
import com.example.trylma.packets.TurnUpdatePacket;
import com.example.trylma.packets.UsernamePacket;
import com.example.trylma.packets.WinPacket;

public class ClientHandler implements Runnable {
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

    private boolean validateNumberOfPlayers(String gameType, int numberOfPlayers) {
        boolean isValid = switch (gameType) {
            case "default" -> numberOfPlayers >= 2 && numberOfPlayers != 5 && numberOfPlayers <= 6;
            case "defaultWithBot" -> numberOfPlayers >= 1 && numberOfPlayers != 4 && numberOfPlayers <= 5;
            case "YinAndYang" -> numberOfPlayers == 2;
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
            System.out.println("Sending packet: " + packet.getClass().getName());
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();
        } catch (IOException e) {
            System.err.println("Failed to transmit packet: " + e.getMessage());
        }
    }

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
            System.err.println("Unknown packet type received: " + packet.getClass().getName());
        }
    }

    private void handleTurnSkipPacket() {
        GameServer.moveToNextTurn();
    }

    private void handleTextMessage(TextMessagePacket packet) {
        String message = packet.getMessageString();
        System.out.println("Received text message: " + message);
        GameServer.broadcastMessage(message, this);
    }

    private void handleMove(MovePacket packet) {
        Move move = packet.getMove();
        System.out.println("Received move: " + move);

        gameManager = GameManagerSingleton.getInstance();

        if (!isPlayerTurn()) {
            transmitInvalidMove(move);
            return;
        }

        if (GameServer.getPlayersWhoWon().contains(this.getPlayer().getUsername())) {
            return;
        }
        
        if (gameManager.isMoveValid(move, this.getPlayer())) {
            gameManager.applyMove(move);

            ChineseCheckersBoard updatedBoard = gameManager.getBoard();
            GameServer.broadcastBoardUpdate(updatedBoard, this);

           if (gameManager.isWinningMove(this.getPlayer())) {
                transmitWin("You win! End of the game");
                GameServer.broadcastMessage("Player " + this.getPlayer().getUsername() + " won! End of the game", this);
                GameServer.addWinner(this.getPlayer().getUsername());
            }

            GameServer.moveToNextTurn();
        } else {
            transmitInvalidMove(move);
            transmitTurnUpdate("Move was invalid. Try again, it's your turn!");
        }
    }

    private void handleUsername(UsernamePacket packet) {
        String username = packet.getUsername();
        System.out.println("Received username: " + username);

        this.player = new GamePlayer(username);
        System.out.println("Client connected: " + username);
    }

    private void handleGameSettings(GameSettingsPacket packet) {
        int numberOfPlayers = packet.getNumberOfPlayers();
        System.out.println("Received number of players: " + numberOfPlayers);
        String gameType = packet.getGameType();
        System.out.println("Received game type: " + gameType);

        validateGameType(gameType);
        GameServer.setGameType(gameType);

        validateNumberOfPlayers(gameType, numberOfPlayers);
        GameServer.setNumberOfPlayers(numberOfPlayers);
        transmitMessage("Game settings applied: " + numberOfPlayers + " players, variant: " + gameType);
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    @Override
    public void run() {
        try {
            setupPlayer();

            synchronized (GameServer.class) {
                GameServer.clientHandlers.add(this);
                GameServer.players.add(this.getPlayer());
                setupGameSettings(GameServer.clientHandlers.size());

                if (GameServer.getGameType().equals("defaultWithBot")) {
                    BotPlayer botPlayer = new BotPlayer("botPlayer");
                    GameServer.setBotPlayer(botPlayer);
                    GameServer.players.add(botPlayer);
                    System.out.println("Bot player added to the game");

                    if (GameServer.players.size() == GameServer.getNumberOfPlayers() + 1) {
                        GameManagerSingleton.setInstance(new StandardGameManager(GameServer.getGameType(), GameServer.getNumberOfPlayers()));
                        GameServer.broadcastMessage("The game is starting!", null);
                        System.out.println("Players: " + GameServer.players);

                        GameServer.moveToNextTurn();

                        gameManager = GameManagerSingleton.getInstance();
                        GameServer.broadcastBoardUpdate(gameManager.getBoard(), this);
                    }
                } else {
                    if (GameServer.players.size() == GameServer.getNumberOfPlayers()) {
                        GameManagerSingleton.setInstance(new StandardGameManager(GameServer.getGameType(), GameServer.getNumberOfPlayers()));
                        GameServer.broadcastMessage("The game is starting!", null);

                        GameServer.moveToNextTurn();

                        gameManager = GameManagerSingleton.getInstance();
                        GameServer.broadcastBoardUpdate(gameManager.getBoard(), this);
                    }
                }
            }

            while (true) {
                ServerPacket serverPacket = (ServerPacket) objectInputStream.readObject();
                handlePacket(serverPacket);
                System.out.println("Received packet: " + serverPacket.getClass().getName());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Connection lost with client: " + this.getPlayer().getUsername());
        } finally {
            cleanup();
        }
    }

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
            System.err.println("Failed to close client socket: " + this.getPlayer().getUsername());
        }
    }
}
