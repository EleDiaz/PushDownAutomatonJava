/*
 * PushDownAutomaton - Transition.java 28/09/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down.model;

import java.util.Optional;

/**
 * A transition, it keeps all information necessary to re-continued trace in case of stop or case of non-determinist
 *
 */
public class TransitionState {

    /**
     * Transition number
     */
    private Optional<Integer> idTransition = Optional.empty();

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

    /**
     * Make a transition, its no carry out sigma transition only saves information to continue in a near future
     */
    public TransitionState(String currState, Tape restInput, Stack lastStack, int idT) {
        stack = lastStack;
        tape = restInput;
        currentState = currState;
        idTransition = Optional.of(idT);
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


    /**
     *
     */
    public Optional<Integer> getIdTransition() {
        return idTransition;
    }

    public TransitionState setIdTransition(Optional<Integer> idTransition) {
        this.idTransition = idTransition;
        return this;
    }
}
