/**
 * PushDownAutomaton - Tracer.java 1/10/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.BitSet;

/**
 * TODO: Commenta algo
 *
 */
public class Tracer {
    private SimpleIntegerProperty fromTransition = new SimpleIntegerProperty(0);
    private SimpleStringProperty state;
    private TransitionState transitionState;

    /**
     * Set of actions that could carry out from this transitionState
     */
    private BitSet actions;

    // TODO: pensar esta estructura como hacer la traza desde ella
    public Tracer() {

    }
}
