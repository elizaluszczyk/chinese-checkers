package com.example.trylma.gui;

import java.io.ObjectOutputStream;

import com.example.trylma.MainApp;
import com.example.trylma.client.GameClient;
import com.example.trylma.interfaces.ClientObserver;
import com.example.trylma.packets.BoardUpdatePacket;
import com.example.trylma.packets.GameSettingsPacket;
import com.example.trylma.packets.InvalidMovePacket;
import com.example.trylma.packets.RequestGameSettingsPacket;
import com.example.trylma.packets.RequestUsernamePacket;
import com.example.trylma.packets.TextMessagePacket;
import com.example.trylma.packets.TurnUpdatePacket;
import com.example.trylma.packets.UsernamePacket;
import com.example.trylma.server.ServerPacket;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GameWindowController implements ClientObserver {


    private final GameClient gameClient = new GameClient("localhost", 58901);

    @FXML
    public void initialize() {
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
        } else if (packet instanceof BoardUpdatePacket) {
            // handleBoardUpdate(boardUpdatePacket);
        } else if (packet instanceof RequestUsernamePacket) {
            handleRequestUsername();
        } else if (packet instanceof RequestGameSettingsPacket) {
            handleRequestGameSettings();
        } else if (packet instanceof TurnUpdatePacket) {
            // handleTurnUpdate(turnUpdatePacket);
        } else if (packet instanceof InvalidMovePacket) {
            // handleInvalidMove(invalidMovePacket);
        } else {
            System.err.println("Unknown packet type received.");
        }
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
}
