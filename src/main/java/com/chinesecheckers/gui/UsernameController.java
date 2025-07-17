package com.chinesecheckers.gui;

import com.chinesecheckers.MainApp;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UsernameController {
  @FXML private TextField usernameTextField;

  @FXML private Label enterUsernameLabel;

  @FXML
  private void handleOkButtonAction() throws IOException {
    String username = usernameTextField.getText();

    if (!username.isEmpty()) {
      MainApp.setUsername(username);
      MainApp.setRoot("/board-view.fxml");
    } else {
      enterUsernameLabel.setText("Username can't be empty!");
    }
  }
}
