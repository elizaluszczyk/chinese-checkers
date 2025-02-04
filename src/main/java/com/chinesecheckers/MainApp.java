package com.chinesecheckers;

import java.io.IOException;

import com.chinesecheckers.client.GameClient;
import com.chinesecheckers.server.GameServer;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static String mode;
    private static Scene scene;
    private static final int PORT = 58901;
    private static String username;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify 'server' or 'client' as an argument.");
            return;
        }

        mode = args[0].toLowerCase();

        switch (mode) {
            case "server" -> startServer();
            case "client" -> launch(args);
            case "client-cli" -> startClientCli();
            default -> System.out.println("Invalid argument. Please specify 'server' or 'client'.");
        }
    }

    private static void startServer() {
        System.out.println("Starting server...");
        GameServer server = new GameServer(PORT);
        server.start();
    }

    public static void startClientCli() {
        new GameClient("localhost", PORT).start(true);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (!"client".equals(mode)) {
            System.out.println("JavaFX is only available in 'client' mode.");
            return;
        }
    
        String fxmlFilename = "/usernameClient.fxml";
        scene = new Scene(loadFXML(fxmlFilename), 910, 1015);
        primaryStage.setScene(scene);
        primaryStage.show();

        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml));
        return fxmlLoader.load();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        MainApp.username = username;
    }
} 
