package main.java;


import main.java.push_down.PushDown;

// TODO
public class Main {
    public static String USAGE = "./push_down file_name    // with definition of automaton";

    public static void main(String[] args) {
        ParseCommands commands = new ParseCommands(args, USAGE);

        // TODO: Open file from interactive input of user

        PushDown pushDown = new PushDown(commands.getString());

        System.out.println("");

        // TODO: Add menu
        // Load
        // Check
        // StepByStep
        // Exit
    }
}
