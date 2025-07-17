package com.chinesecheckers.client;

import com.chinesecheckers.board.Move;
import com.chinesecheckers.exceptions.InvalidMoveException;
import com.chinesecheckers.interfaces.ClientObserver;
import com.chinesecheckers.packets.BoardUpdatePacket;
import com.chinesecheckers.packets.FieldData;
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
import com.chinesecheckers.parsers.StandardMoveParser;
import com.chinesecheckers.server.ServerPacket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class GameClient {
  private final String serverAddress;
  private final int port;
  private final StandardMoveParser moveParser = new StandardMoveParser();
  private final Queue<String> queue = new LinkedList<>();
  private boolean waitingForUsername = false;
  private boolean waitingForGameSettings = false;
  public ObjectOutputStream objectOutputStream;
  public ObjectInputStream objectInputStream;

  public final ArrayList<ClientObserver> clientObservers = new ArrayList<>();

  public GameClient(String serverAddress, int port) {
    this.serverAddress = serverAddress;
    this.port = port;
  }

  public void addObserver(ClientObserver observer) {
    this.clientObservers.add(observer);
  }

  public void removeObserver(ClientObserver observer) {
    this.clientObservers.remove(observer);
  }

  public void emitTurnSkipPacket() {
    sendPacketToServer(new TurnSkipPacket(), objectOutputStream);
  }

  public void start(boolean takeUserInput) {
    try (Socket socket = new Socket(serverAddress, port); ) {

      this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      this.objectInputStream = new ObjectInputStream(socket.getInputStream());

      System.out.println("Connected to server: " + serverAddress + ":" + port);

      if (takeUserInput) {
        Thread receiveThread =
            new Thread(
                () -> {
                  try {
                    while (true) {
                      ServerPacket serverPacket = (ServerPacket) objectInputStream.readObject();
                      System.out.println(serverPacket.toString());
                      handlePacket(serverPacket);
                    }
                  } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Connection to server lost: " + e.getMessage());
                  }
                });
        receiveThread.start();

        System.out.println("You can send messages now (type 'exit' to quit)");

        while (true) {
          getInputFromPlayer("");
          if (queue.peek() == null) {
            continue;
          }
          if (queue.peek().equalsIgnoreCase("exit")) {
            System.out.println("Disconnecting...");
            break;
          }

          handleUserInput(objectOutputStream);
        }
      } else {
        try {
          while (true) {
            ServerPacket serverPacket = (ServerPacket) objectInputStream.readObject();
            handlePacket(serverPacket);
          }
        } catch (IOException | ClassNotFoundException e) {
          System.err.println("Connection to server lost: " + e.getMessage());
        }
      }

    } catch (IOException e) {
      System.err.println("Error connecting to server: " + e.getMessage());
    }
  }

  public void getInputFromPlayer(String prompt) {
    try {
      BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
      System.out.print(prompt);
      String input = consoleReader.readLine();
      if (input == null) {
        System.err.println("Input stream closed. No more input available.");
        return;
      }
      this.queue.add(input);
    } catch (IOException e) {
      System.err.println("Error reading input: " + e.getMessage());
    }
  }

  /*
   * Each time after a user inputs a string this method is called. It decides what to do with the string from the user
   */
  private void handleUserInput(ObjectOutputStream objectOutputStream) {
    if (this.waitingForUsername) {
      String userInputString = queue.remove();
      System.out.println("Ill send this input as a string message, since the flag is set");
      UsernamePacket usernamePacket = new UsernamePacket(userInputString);
      sendPacketToServer(usernamePacket, objectOutputStream);
      this.waitingForUsername = false;
      return;
    } else if (this.waitingForGameSettings) {
      if (queue.size() < 2) return;

      String numberOfPlayersString = queue.remove();
      String gameTypeString = queue.remove();

      int numberOfPlayers;

      try {
        numberOfPlayers = Integer.parseInt(numberOfPlayersString);
      } catch (NumberFormatException e) {
        System.out.println("Invalid number of players, enter settings again");
        return;
      }

      GameSettingsPacket gameSettingsPacket =
          new GameSettingsPacket(numberOfPlayers, gameTypeString);
      sendPacketToServer(gameSettingsPacket, objectOutputStream);

      this.waitingForGameSettings = false;
    }
    try {
      if (queue.isEmpty()) return;
      String userInputString = queue.remove();
      ServerPacket serverPacket = parseInput(userInputString);
      if (serverPacket != null) {
        sendPacketToServer(serverPacket, objectOutputStream);
      }
    } catch (InvalidMoveException e) {
      System.err.println("Invalid input: " + e.getMessage());
    } catch (NoSuchElementException e) {
      System.err.println("Missing element:" + e.getMessage());
    }
  }

  public void sendPacketToServer(ServerPacket packet, ObjectOutputStream objectOutputStream) {
    try {
      objectOutputStream.writeObject(packet);
      objectOutputStream.flush();
    } catch (IOException e) {
      System.err.println("Error sending packet to server: " + e.getMessage());
    }
  }

  private ServerPacket parseInput(String input) throws InvalidMoveException {
    if (input.startsWith("MOVE")) {
      return new MovePacket(moveParser.parseMove(input));
    }
    return new TextMessagePacket(input);
  }

  private void notifyAllOnPacket(ServerPacket packet) {
    for (ClientObserver observer : this.clientObservers) {
      observer.notifyPacket(packet);
    }
  }

  private void handlePacket(ServerPacket packet) throws IOException {
    if (packet instanceof TextMessagePacket textMessagePacket) {
      handleTextMessage(textMessagePacket);
    } else if (packet instanceof BoardUpdatePacket boardUpdatePacket) {
      handleBoardUpdate(boardUpdatePacket);
    } else if (packet instanceof RequestUsernamePacket requestUsernamePacket) {
      handleRequestUsername(requestUsernamePacket);
    } else if (packet instanceof RequestGameSettingsPacket requestGameSettingsPacket) {
      handleRequestGameSettings(requestGameSettingsPacket);
    } else if (packet instanceof TurnUpdatePacket turnUpdatePacket) {
      handleTurnUpdate(turnUpdatePacket);
    } else if (packet instanceof WinPacket) {
      handleWinPacket();
    } else if (packet instanceof InvalidMovePacket invalidMovePacket) {
      handleInvalidMove(invalidMovePacket);
    } else if (packet instanceof InvalidGameSettingsPacket invalidGameSettingsPacket) {
      handleInvalidGameSettings(invalidGameSettingsPacket);
    } else {
      System.err.println("Unknown packet type received.");
      return;
    }
    this.notifyAllOnPacket(packet);
  }

  private void handleWinPacket() {
    System.out.println("You won");
  }

  private void handleTextMessage(TextMessagePacket packet) {
    String message = packet.getMessage();
    System.out.println("Received text message: " + message);
  }

  private void handleBoardUpdate(BoardUpdatePacket packet) {
    // ChineseCheckersBoard board = packet.getBoard();
    // board.printBoard();
    for (ArrayList<FieldData> row : packet.getBoard()) {
      for (FieldData field : row) {
        if (field.isActive()) {
          System.out.print(field.toString());
        } else {
          System.out.print(" ");
        }
      }
      System.out.println();
    }
    System.out.println();
    System.out.println("Received board update: ");
  }

  private void handleRequestUsername(RequestUsernamePacket packet) throws IOException {
    if (this.waitingForUsername) return;
    String message = packet.getMessage();
    System.out.println("We be handling request username packet");
    this.waitingForUsername = true;

    System.out.print(message);
  }

  private void handleRequestGameSettings(RequestGameSettingsPacket packet) {
    if (this.waitingForGameSettings) return;
    String numberOfPlayersMessage = packet.getNumberOfPlayersMessage();

    String gameTypeMessage = packet.getGameTypeMessage();

    System.out.println(numberOfPlayersMessage);
    System.out.println(gameTypeMessage);

    this.waitingForGameSettings = true;
  }

  private void handleTurnUpdate(TurnUpdatePacket packet) {
    String message = packet.getMessage();
    System.out.println("Received turn update: " + message);
  }

  private void handleInvalidMove(InvalidMovePacket packet) {
    Move invalidMove = packet.getInvalidMove();
    System.out.println("Received invalid move: " + invalidMove);
  }

  private void handleInvalidGameSettings(InvalidGameSettingsPacket packet) {
    String message = packet.getMessage();
    System.out.println("Received invalid game settings: " + message);
    this.waitingForGameSettings = true;
    handleUserInput(objectOutputStream);
  }

  public ObjectOutputStream getObjectOutputStream() {
    return this.objectOutputStream;
  }
}
