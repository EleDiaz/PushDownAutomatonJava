/**
 * PushDownAutomaton - Transition.java 28/09/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package es.ull.cc.automaton.push_down;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * TODO: Commenta algo
 *
 */
public class Transition {
    private int currentState;
    private String tape;
    private Vector<Character> stack;

    private Transition(PushDown.Output output, String restInput, Vector<Character> stack) {
        // output.stackItems

    }

    public static Vector<Transition> make(Vector<PushDown.Output> outputs, String restInput, Vector<Character> stack) {
        return outputs.stream().map((output) -> new Transition(output, restInput, stack)).collect(Collectors.toCollection(Vector::new));
    }
}
