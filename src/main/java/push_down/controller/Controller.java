/**
 * PushDownAutomaton - Controller.java 1/10/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import main.java.push_down.model.PushDown;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * TODO: Commenta algo
 *
 */
public class Controller implements Initializable {

    PushDown pushDown;

    @FXML //  fx:id="checkString"
    private Button checkString;

    @FXML //  fx:id="step"
    private Button step;

    @FXML //  fx:id="reset"
    private Button reset;

    @FXML //  fx:id="tapeInput"
    private TextField tapeInput;

    @FXML //  fx:id="infoLabel"
    private Label infoLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert checkString != null : "fx:id=\"checkString\" was not injected: check your FXML file 'Main.fxml'.";
        assert step != null : "fx:id=\"step\" was not injected: check your FXML file 'Main.fxml'.";
        assert reset != null : "fx:id=\"reset\" was not injected: check your FXML file 'Main.fxml'.";
        assert tapeInput != null : "fx:id=\"tapeInput\" was not injected: check your FXML file 'Main.fxml'.";
    }



    public void onCloseApp() {
        System.exit(0);
    }

    public void onOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a push down automaton file (.pda)");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Push down Files", "*.pda"),
            new FileChooser.ExtensionFilter("All Files", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(reset.getScene().getWindow());
        if (selectedFile != null) {
            try {
                pushDown = new PushDown(selectedFile.toString());
                infoLabel.setText("Loaded a push down file");
            } catch (Exception e) {
                e.getMessage();
                // TODO: Abstract this part to other class, it is necessary in other interactions
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.getDialogPane().getButtonTypes().add(new ButtonType("Got it!", ButtonBar.ButtonData.CANCEL_CLOSE));
                dialog.setTitle("An Error happened");
                dialog.setHeaderText("File error");
                dialog.setContentText(e.getMessage());
                dialog.showAndWait();
            }
        }
    }

    public void onCheckString() {

        try {
            boolean test = pushDown.checkString(tapeInput.getText());
            infoLabel.setText(test? "Input belong to language" : "Not belong");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
