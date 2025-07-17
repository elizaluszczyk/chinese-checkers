package com.chinesecheckers.gui;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import com.chinesecheckers.MainApp;
import com.chinesecheckers.board.Move;
import com.chinesecheckers.client.GameClient;
import com.chinesecheckers.interfaces.ClientObserver;
import com.chinesecheckers.packets.BoardUpdatePacket;
import com.chinesecheckers.packets.FieldData;
import com.chinesecheckers.packets.GameSettingsPacket;
import com.chinesecheckers.packets.InvalidMovePacket;
import com.chinesecheckers.packets.MovePacket;
import com.chinesecheckers.packets.RequestGameSettingsPacket;
import com.chinesecheckers.packets.RequestUsernamePacket;
import com.chinesecheckers.packets.TextMessagePacket;
import com.chinesecheckers.packets.TurnUpdatePacket;
import com.chinesecheckers.packets.UsernamePacket;
import com.chinesecheckers.packets.WinPacket;
import com.chinesecheckers.server.ServerPacket;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BoardController implements ClientObserver {
    @FXML
    private Pane mainPane;
    private ArrayList<ArrayList<FieldData>> board = new ArrayList<>();
    private final GameClient gameClient = new GameClient("localhost", 58901);
    private ClickedField clickedField = null;
    private Queue<Color> colorQueue = new LinkedList<>();
    private HashMap<String, Color> playerIdToColor = new HashMap<>();

    @FXML
    private Button skipButton;

    @FXML
    private Label turnLabel;

    @FXML
    private Label winLabel;

    @FXML
    void onSkipButtonPress(ActionEvent event) {
        setGuiOponentsTurn();

        gameClient.emitTurnSkipPacket();
    }

    @FXML
    public void initialize() throws IOException {
        Platform.runLater(() -> {
            boardSetup(mainPane);
        });
        gameClient.addObserver(this);
        Thread receiveThread = new Thread(() -> {
            gameClient.start(false);
        });
        receiveThread.start();

        colorQueue.add(Color.LIGHTBLUE);
        colorQueue.add(Color.LIGHTGREEN);
        colorQueue.add(Color.CORAL);
        colorQueue.add(Color.CYAN);
        colorQueue.add(Color.LIGHTSALMON);
        colorQueue.add(Color.MEDIUMPURPLE);
    }

    @Override
    public void notifyPacket(ServerPacket packet) {
        if (packet instanceof TextMessagePacket) {
            // handleTextMessage(textMessagePacket);
        } else if (packet instanceof BoardUpdatePacket boardUpdatePacket) {
            handleBoardUpdate(boardUpdatePacket);
        } else if (packet instanceof RequestUsernamePacket) {
            handleRequestUsername();
        } else if (packet instanceof RequestGameSettingsPacket) {
            handleRequestGameSettings();
        } else if (packet instanceof TurnUpdatePacket) {
            handleTurnUpdate();
        } else if (packet instanceof WinPacket) {
            handleWin();
        } else if (packet instanceof InvalidMovePacket) {
            // handleInvalidMove(invalidMovePacket);
        } else {
            System.err.println("Unknown packet type received.");
        }
    }

    public void handleWin() {
        Platform.runLater(() -> {
            winLabel.setVisible(true);
            skipButton.setVisible(false);
            turnLabel.setVisible(false);
        });
    }

    public void handleTurnUpdate() {
        Platform.runLater(() -> {
            // make button visible
            skipButton.setVisible(true);
            // turn color label
            turnLabel.setStyle("-fx-background-color: rgb(107, 190, 48); -fx-background-radius: 8;");
            // change text label
            turnLabel.setText("Your turn");
        });
    }

    public void setGuiOponentsTurn() {
        Platform.runLater(() -> {
            skipButton.setVisible(false);
            // turn color label
            turnLabel.setStyle("-fx-background-color:  rgb(248, 8, 8); -fx-background-radius: 8;");
            // change text label
            turnLabel.setText("Oponents turn");
        });
    }

    
    public void handleBoardUpdate(BoardUpdatePacket packet) {
        ArrayList<ArrayList<FieldData>> newBoard = packet.getBoard();

        board = newBoard;
        Platform.runLater(() -> {
            createTriangleBoard(mainPane);
        });    
    }
    
    private void handleRequestUsername() {
        try {
            ObjectOutputStream objectOutputStream = gameClient.getObjectOutputStream();

            UsernamePacket usernamePacket = new UsernamePacket(MainApp.getUsername());
            gameClient.sendPacketToServer(usernamePacket, objectOutputStream);
            
        } catch (Exception e) {
            System.err.println("Error sending username to server: " + e.getMessage());
        }
    }

    private void handleRequestGameSettings() {
        System.out.println("GUI would like to let you know that we got a request for game settings");
        Platform.runLater(() -> { 
            openGameSettingsDialog();
        });
    }

    private void emitGameSettings(String gameType, int playerCount) {
        try {
            ObjectOutputStream objectOutputStream = gameClient.getObjectOutputStream();

            GameSettingsPacket usernamePacket = new GameSettingsPacket(playerCount, gameType);

            gameClient.sendPacketToServer(usernamePacket, objectOutputStream);
            
        } catch (Exception e) {
            System.err.println("Error sending game settings to server: " + e.getMessage());
        }
    }

    private void emitMove(Move move) {
        try {
            ObjectOutputStream objectOutputStream = gameClient.getObjectOutputStream();

            MovePacket packet = new MovePacket(move);

            gameClient.sendPacketToServer(packet, objectOutputStream);
            
        } catch (Exception e) {
            System.err.println("Error sending game settings to server: " + e.getMessage());
        }
    }

    private void openGameSettingsDialog() {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Game Settings");
        dialog.setHeaderText("Enter Game Settings");
    
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
    
        ChoiceBox<Integer> playerCountDropdown = new ChoiceBox<>();
        playerCountDropdown.getItems().addAll(2, 3, 4, 6);
        playerCountDropdown.setValue(2);

        playerCountDropdown.setMinWidth(100); 
        playerCountDropdown.setPrefWidth(100);
    
        ChoiceBox<String> gameTypeDropdownField = new ChoiceBox<>();
        gameTypeDropdownField.getItems().addAll("default", "defaultWithBot", "YinAndYang");
        gameTypeDropdownField.setValue("default");
    
        gameTypeDropdownField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("YinAndYang".equals(newValue)) {
                playerCountDropdown.getItems().setAll(2);
                playerCountDropdown.setValue(2);
                playerCountDropdown.setDisable(true);
            } else if ("defaultWithBot".equals(newValue)) {
                playerCountDropdown.getItems().setAll(1, 2, 3, 5);
                playerCountDropdown.setValue(1);
                playerCountDropdown.setDisable(false);
            } else {
                playerCountDropdown.getItems().setAll(2, 3, 4, 6);
                playerCountDropdown.setValue(2);
                playerCountDropdown.setDisable(false);
            }
        });

        grid.add(new Label("Player Count:"), 0, 2);
        grid.add(playerCountDropdown, 1, 2);
        grid.add(new Label("Game type:"), 0, 3);
        grid.add(gameTypeDropdownField, 1, 3);
    
        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                emitGameSettings(gameTypeDropdownField.getValue(), playerCountDropdown.getValue());
            }
            return null;
        });
    
        dialog.showAndWait();
    }


    public void boardSetup(Pane pane) {
        createTriangleBoard(pane);
    }

    public void createTriangleBoard(Pane pane) {
        pane.getChildren().clear();
        int radius = 30;
        int offset = 5;

        int yCoordinate = radius + offset;
        for (int rowIndex = 0; rowIndex < board.size(); rowIndex++) {
            int xCoordinate = radius + offset;
            for (int columnIndex = 0; columnIndex < board.get(0).size(); columnIndex++) {
                FieldData field = board.get(rowIndex).get(columnIndex);
                
                

                if (!field.isActive()) {
                    xCoordinate += radius + offset;
                    continue;
                }
                Circle circle = new Circle(xCoordinate, yCoordinate, radius);
                circle.setFill(Color.rgb(98, 98, 98));

                if (field.isOccupied()) {
                    stylePawn(field, circle, offset);
                    
                }

                circle.setOnMouseClicked(event -> {
                    handleClickOnBoard(new ClickedField(field.getX(), field.getY()));
                });

                pane.getChildren().add(circle);
                xCoordinate += radius + offset;
            }
            yCoordinate += radius * Math.sqrt(3) + offset;
        }
    }

    public void stylePawn(FieldData field, Circle circle, int offset) {
        if (!playerIdToColor.containsKey(field.getOwnerId())) {
            Color color = colorQueue.poll();

            if (Objects.isNull(color)) color = Color.RED;

            playerIdToColor.put(field.getOwnerId(), color);
        }

        circle.setFill(playerIdToColor.get(field.getOwnerId()));

        if (MainApp.getUsername().equals(field.getOwnerId())) {
            circle.setStroke(Color.GOLD);
            circle.setStrokeWidth(offset / 2);
        }
    }

    private void handleClickOnBoard(ClickedField newClickedField) {

        if (clickedField == null) {
            clickedField = newClickedField;
            return;
        }

        Move move = new Move(
            clickedField.getX(), clickedField.getY(),
            newClickedField.getX(), newClickedField.getY()
        );
        
        emitMove(move);
        clickedField = null;

        setGuiOponentsTurn();
    }

    private class ClickedField {
        int x, y;

        public ClickedField(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
