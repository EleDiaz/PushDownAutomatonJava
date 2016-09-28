/**
 * PushDownAutomaton - Transition.java 28/09/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down;

import java.util.ArrayList;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * A transition, it keeps all information necessary to re-continued trace in case of stop or case of no-determinist
 *
 */
public class Transition {
    private int currentState;
    private String tape;
    private ArrayList<Character> stack;

    private Transition(PushDown.Output output, String restInput, ArrayList<Character> stack) {
        this.stack = stack;

        for (int i = output.stackItems.length() - 1; i >= 0; i--) { // EXPLAIN: I use a vector like stack extracting the last items
            this.stack.add(output.stackItems.charAt(i));
        }

        tape = restInput;

        currentState = output.state;

    }

    public static Vector<Transition> make(ArrayList<PushDown.Output> outputs, String restInput, ArrayList<Character> stack) {
        return outputs.stream().map((output) -> new Transition(output, restInput, stack)).collect(Collectors.toCollection(Vector::new));
    }


    public Character popStack() {
        Character lastStackChar = stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);
        return lastStackChar;
    }

    //// Setters and Getters

    /**
     *
     */
    public int getCurrentState() {
        return currentState;
    }

    public Transition setCurrentState(int currentState) {
        this.currentState = currentState;
        return this;
    }

    /**
     *
     */
    public String getTape() {
        return tape;
    }

    public Transition setTape(String tape) {
        this.tape = tape;
        return this;
    }

    /**
     *
     */
    public ArrayList<Character> getStack() {
        return stack;
    }

    public Transition setStack(ArrayList<Character> stack) {
        this.stack = stack;
        return this;
    }

}
