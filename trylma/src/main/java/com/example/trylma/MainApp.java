package com.example.trylma;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application{

public static void main(String[]args){
        launch();
        }

@Override
public void start(Stage primaryStage) throws IOException {
    FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/board-view.fxml"));
    Pane root = loader.load();
    BoardController controller = new BoardController();
    Scene scene = new Scene(root, 800, 800);
    controller.boardSetup(root);
    primaryStage.setTitle("Chińskie Warcaby");
    primaryStage.setScene(scene);
    primaryStage.show();
}
}

