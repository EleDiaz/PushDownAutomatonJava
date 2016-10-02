/*
 * PushDownAutomaton - Transition.java 28/09/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down.model;

/**
 * A transition, it keeps all information necessary to re-continued trace in case of stop or case of non-determinist
 *
 */
public class TransitionState {

    /**
     * Current state in this transition
     */
    private String currentState;

    /**
     * Remaining tape
     */
    private Tape tape;

    /**
     * Current stack
     */
    private Stack stack;

    /**
     * Copy constructor
     */
    public TransitionState(TransitionState transitionState) {
        currentState = transitionState.getCurrentState();
        tape = new Tape(transitionState.getTape());
        stack = new Stack(transitionState.getStack());
    }

    /**
     * Make a transition, its no carry out sigma transition only saves information to continue in a near future
     */
    public TransitionState(String currState, Tape restInput, Stack lastStack) {
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

    public TransitionState setCurrentState(String currentState) {
        this.currentState = currentState;
        return this;
    }

    /**
     *
     */
    public Tape getTape() {
        return tape;
    }

    public TransitionState setTape(Tape tape) {
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
