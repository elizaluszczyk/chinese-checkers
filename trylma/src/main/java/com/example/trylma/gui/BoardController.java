package com.example.trylma.gui;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.example.trylma.MainApp;
import com.example.trylma.board.Move;
import com.example.trylma.client.GameClient;
import com.example.trylma.interfaces.ClientObserver;
import com.example.trylma.packets.BoardUpdatePacket;
import com.example.trylma.packets.FieldData;
import com.example.trylma.packets.GameSettingsPacket;
import com.example.trylma.packets.InvalidMovePacket;
import com.example.trylma.packets.MovePacket;
import com.example.trylma.packets.RequestGameSettingsPacket;
import com.example.trylma.packets.RequestUsernamePacket;
import com.example.trylma.packets.TextMessagePacket;
import com.example.trylma.packets.TurnUpdatePacket;
import com.example.trylma.packets.UsernamePacket;
import com.example.trylma.packets.WinPacket;
import com.example.trylma.server.ServerPacket;

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
        playerCountDropdown.getItems().add(2);
        playerCountDropdown.getItems().add(3);
        playerCountDropdown.getItems().add(4);
        playerCountDropdown.getItems().add(6);
        playerCountDropdown.setValue(2);
    
        ChoiceBox<String> gameTypeDropdownField = new ChoiceBox<>();
        gameTypeDropdownField.getItems().add("default");
        gameTypeDropdownField.setValue("default");
    
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
                    circle.setFill(Color.RED);
                    if (MainApp.getUsername().equals(field.getOwnerId())) {
                        circle.setStroke(Color.GOLD);
                        circle.setStrokeWidth(offset / 2);
                    }
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

    private void handleClickOnBoard(ClickedField newClickedField) {

        if (clickedField == null) {
            clickedField = newClickedField;
            return;
        }

        Move move = new Move(
            clickedField.getY(), clickedField.getX(),
            newClickedField.getY(), newClickedField.getX()
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
