package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource("Main.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("PushDown Automaton");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
/*
// TODO
public class Main {
    public static String USAGE = "./push_down file_name    // with definition of automaton";

    public static void main(String[] args) {
        ParseCommands commands = new ParseCommands(args, USAGE);

        // TODO: Open file from interactive input of user

        PushDown pushDown = new PushDown(commands.getString());

        System.out.println("Tachan");

        // TODO: Add menu
        // Load
        // Check
        // StepByStep
        // Exit
    }
}
*/
