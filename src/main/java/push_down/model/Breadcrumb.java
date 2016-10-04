/**
 * PushDownAutomaton - Breadcrumb.java 1/10/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.BitSet;

/**
 * Represent a readable output of trace of execution
 */
public class Breadcrumb {
    /**
     * Number of trace for simple tracking purpose
     */
    private SimpleIntegerProperty traceN = new SimpleIntegerProperty(0);
    /**
     * Number of trace for simple tracking purpose
     */
    private SimpleStringProperty lastTraceN = new SimpleStringProperty("N/A");
    /**
     * Transition used to arrive to current trace for simple tracking purpose
     */
    private SimpleStringProperty usedTransition = new SimpleStringProperty("N/A");
    /**
     * State
     */
    private SimpleStringProperty state;
    /**
     * Tape input
     */
    private SimpleStringProperty tape;
    /**
     * Current stack status
     */
    private SimpleStringProperty stack;
    /**
     * Avialable actions to make from this transition
     */
    private SimpleStringProperty actions = new SimpleStringProperty();

    public Breadcrumb(TransitionState transition, BitSet actions, int traceN, int lastTrace) {
        this.traceN = new SimpleIntegerProperty(traceN);
        this.lastTraceN = new SimpleStringProperty(Integer.toString(lastTrace));
        transition.getIdTransition().ifPresent(id ->
            this.usedTransition  = new SimpleStringProperty(id.toString()));
        this.state = new SimpleStringProperty(transition.getCurrentState());
        this.tape = new SimpleStringProperty(transition.getTape().isEmpty() ? "ø" : transition.getTape().getTape());
        this.stack = new SimpleStringProperty(transition.getStack().isEmpty() ? "ø" : transition.getStack().getStack());

        StringBuilder aux = new StringBuilder();
        actions.stream().forEach(idTransition ->
            aux.append(idTransition).append(", ")
        );
        this.actions.set(aux.toString().isEmpty() ? "No there're more transitions": aux.toString());
    }

    /// Getters and Setters, needed for table view

    public int getTraceN() {
        return traceN.get();
    }

    public SimpleIntegerProperty traceNProperty() {
        return traceN;
    }

    public void setTraceN(int traceN) {
        this.traceN.set(traceN);
    }

    public String getLastTraceN() {
        return lastTraceN.get();
    }

    public SimpleStringProperty lastTraceNProperty() {
        return lastTraceN;
    }

    public void setLastTraceN(String lastTraceN) {
        this.lastTraceN.set(lastTraceN);
    }

    public String getUsedTransition() {
        return usedTransition.get();
    }

    public SimpleStringProperty usedTransitionProperty() {
        return usedTransition;
    }

    public void setUsedTransition(String usedTransition) {
        this.usedTransition.set(usedTransition);
    }

    public String getState() {
        return state.get();
    }

    public SimpleStringProperty stateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getTape() {
        return tape.get();
    }

    public SimpleStringProperty tapeProperty() {
        return tape;
    }

    public void setTape(String tape) {
        this.tape.set(tape);
    }

    public String getStack() {
        return stack.get();
    }

    public SimpleStringProperty stackProperty() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack.set(stack);
    }

    public String getActions() {
        return actions.get();
    }

    public SimpleStringProperty actionsProperty() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions.set(actions);
    }
}
