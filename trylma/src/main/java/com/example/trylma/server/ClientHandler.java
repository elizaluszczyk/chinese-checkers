package com.example.trylma.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.example.trylma.board.Move;
import com.example.trylma.game.GamePlayer;
import com.example.trylma.game.StandardGameManager;
import com.example.trylma.interfaces.Board;
import com.example.trylma.interfaces.GameManager;
import com.example.trylma.interfaces.Player;
import com.example.trylma.packets.BoardUpdatePacket;
import com.example.trylma.packets.GameSettingsPacket;
import com.example.trylma.packets.InvalidMovePacket;
import com.example.trylma.packets.MovePacket;
import com.example.trylma.packets.RequestGameSettingsPacket;
import com.example.trylma.packets.RequestUsernamePacket;
import com.example.trylma.packets.TextMessagePacket;
import com.example.trylma.packets.TurnUpdatePacket;
import com.example.trylma.packets.UsernamePacket;

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

    public void transmitMessage(String message) {
        transmitPacket(new TextMessagePacket(message));
    }

    public void transmitMove(Move move) {
        transmitPacket(new MovePacket(move));
    }

    public void transmitBoardUpdate(Board board) {
        transmitPacket(new BoardUpdatePacket(board));
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
        } else {
            System.err.println("Unknown packet type received: " + packet.getClass().getName());
        }
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
            transmitMessage("It's not your turn!");
            return;
        }

        if (gameManager.isMoveValid(move)) {
            gameManager.applyMove(move);

            Board updatedBoard = gameManager.getBoard();
            GameServer.broadcastBoardUpdate(updatedBoard, this);

            GameServer.moveToNextTurn();
        } else {
            this.transmitInvalidMove(move);
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

        GameServer.setNumberOfPlayers(numberOfPlayers);
        GameManagerSingleton.setInstance(new StandardGameManager(gameType, numberOfPlayers));
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
                setupGameSettings(GameServer.clientHandlers.size());

                if (GameServer.clientHandlers.size() == GameServer.getNumberOfPlayers()) {
                    GameServer.broadcastMessage("The game is starting!", null);
                    GameServer.moveToNextTurn();
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
            if (player != null) {
                GameServer.broadcastMessage(player.getUsername() + " has left the game.", null);
            }
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Failed to close client socket: " + e.getMessage());
        }
    }
}
