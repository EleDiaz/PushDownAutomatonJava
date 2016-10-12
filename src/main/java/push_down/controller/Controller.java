/**
 * PushDownAutomaton - Controller.java 1/10/16
 *
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import main.java.push_down.model.Breadcrumb;
import main.java.push_down.model.PushDown;
import main.java.push_down.model.RawSigmaTransition;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

/**
 * TODO: Highlight End states q*
 */
public class Controller implements Initializable {

    private PushDown pushDown;

    @FXML //  fx:id="checkString"
    private Button checkString;

    @FXML //  fx:id="tapeInput"
    private TextField tapeInput;

    @FXML //  fx:id="infoLabel"
    private Label infoLabel;

    @FXML //  fx:id="transitionsTable"
    private TableView<RawSigmaTransition> transitionsTable;

    @FXML //  fx:id="traceTable"
    private TableView<Breadcrumb> traceTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert checkString != null : "fx:id=\"checkString\" was not injected: check your FXML file 'Main.fxml'.";
        assert tapeInput != null : "fx:id=\"tapeInput\" was not injected: check your FXML file 'Main.fxml'.";
        assert transitionsTable != null : "fx:id=\"transitionTable\" was not injected: check your FXML file 'Main.fxml'.";

        // Set order of display trace
        transitionsTable.sortPolicyProperty().set(t -> {
            Comparator<RawSigmaTransition> comparator = (r1, r2) ->
                new Integer(r1.getNumTransition()).compareTo(r2.getNumTransition());
            FXCollections.sort(t.getItems(), comparator);
            return true;
        });
    }

    /**
     * Close app
     */
    public void onCloseApp() {
        System.exit(0);
    }

    /**
     * Dispatch a file dialog to open a .pda files
     */
    public void onOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a push down automaton file (.pda)");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Push down Files", "*.pda"),
            new FileChooser.ExtensionFilter("All Files", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(checkString.getScene().getWindow());
        if (selectedFile != null) {
            try {
                pushDown = new PushDown(selectedFile.toString());
                infoLabel.setText("Loaded a push down file");
            } catch (Exception e) {
                showErrorDialog(e.getMessage());
            }
            transitionsTable.setItems(FXCollections.observableArrayList(RawSigmaTransition.getRawTransitions(pushDown.getTransitions())));
            transitionsTable.sort();
        }
    }

    /**
     * Check the string
     */
    public void onCheckString() {

        try {
            ArrayList<Breadcrumb> trace = new ArrayList<>();
            boolean test = pushDown.checkString(tapeInput.getText(), trace);
            infoLabel.setText(test? "Input belong to language" : "Not belong");
            traceTable.setItems(FXCollections.observableArrayList(trace));
        } catch (Exception e) {
            showErrorDialog(e.getMessage());
        }

    }

    /**
     * Show a error dialog with specified text
     */
    private void showErrorDialog(String error) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("Got it!", ButtonBar.ButtonData.CANCEL_CLOSE));
        dialog.setTitle("An Error happened");
        dialog.setHeaderText("File error");
        dialog.setContentText(error);
        dialog.showAndWait();
    }
}
