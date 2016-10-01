/*
 * PushDownAutomaton - Transition.java 28/09/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down.model;

/**
 * A transition, it keeps all information necessary to re-continued trace in case of stop or case of no-determinist
 *
 */
public class Transition {
    private String currentState;
    private Tape tape;
    private Stack stack;

    public Transition(Transition transition) {
        currentState = transition.getCurrentState();
        tape = new Tape(transition.getTape());
        stack = new Stack(transition.getStack());
    }

    public Transition(String currState, Tape restInput, Stack lastStack) {
        stack = lastStack;

        tape = restInput;

        currentState = currState;
    }

    //// Setters and Getters

    /**
     *
     */
    public String getCurrentState() {
        return currentState;
    }

    public Transition setCurrentState(String currentState) {
        this.currentState = currentState;
        return this;
    }

    /**
     *
     */
    public Tape getTape() {
        return tape;
    }

    public Transition setTape(Tape tape) {
        this.tape = tape;
        return this;
    }

    /**
     *
     */
    public Stack getStack() {
        return stack;
    }

}
