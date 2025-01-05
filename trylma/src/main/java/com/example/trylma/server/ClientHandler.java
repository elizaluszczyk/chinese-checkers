package com.example.trylma.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.example.trylma.board.Move;
import com.example.trylma.game.GamePlayer;
import com.example.trylma.interfaces.Board;
import com.example.trylma.packets.BoardUpdatePacket;
import com.example.trylma.packets.InvalidMovePacket;
import com.example.trylma.packets.MovePacket;
import com.example.trylma.packets.TextMessagePacket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;
    private GamePlayer player;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    public void setPlayer(GamePlayer player) {
        this.player = player;
    }

    public GamePlayer getPlayer() {
        return player;
    }

    public TextMessagePacket receiveTextMessage() throws IOException, ClassNotFoundException {
        Object received = objectInputStream.readObject();
        if (received instanceof TextMessagePacket packet) {
            handleTextMessage(packet);
            return packet;
        } else {
            throw new ClassNotFoundException("Unexpected packet type: " + received.getClass().getName());
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

    public void transmitPacket(ServerPacket packet) {
        try {
        System.out.println("Sending packet: " + packet.getClass().getName());
           objectOutputStream.writeObject(packet);
           objectOutputStream.flush();
        } catch (IOException e) {
            System.err.println("Failed to transmit packet:" + e.getMessage());
        } 
    }

    private void handlePacket(ServerPacket packet) {
        if (packet instanceof TextMessagePacket textMessagePacket) {
            handleTextMessage(textMessagePacket);
        } else if (packet instanceof MovePacket movePacket) {
            handleMove(movePacket);
        } else {
            System.err.println("Unknown packet type received.");
        }
    }
    
    private void handleTextMessage(TextMessagePacket packet) {
        String message = packet.getMessageString();
        System.out.println("Received text message: " + message);
    }

    private void handleMove(MovePacket packet) {
        Move move = packet.getMove();
        System.out.println("Received move: " + move);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object received = objectInputStream.readObject();
                System.out.println("Received object: " + received.getClass().getName());
                if (received instanceof ServerPacket packet) {
                    handlePacket(packet);
                } else {
                    System.err.println("Unexpected object type: " + received.getClass().getName());
                }
            }
        } catch (IOException e) {
            System.err.println("Connection lost with client: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } finally {
            GameServer.clientHandlers.remove(this);
            if (player != null) {
                GameServer.broadcastMessage(player.getUsername() + " has left the game.", null);
            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Failed to close client socket: " + e.getMessage());
            }
        }
    }
}
