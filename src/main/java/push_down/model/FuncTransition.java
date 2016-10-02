/**
 * PushDownAutomaton - FuncTransition.java 30/09/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Vector;

/**
 * A wrapper for HashMap
 *
 */
public class FuncTransition {

    private HashMap<Input, ArrayList<Output>> transitions = new HashMap<>();

    /**
     * Auxiliary to enumerate each transition
     */
    private int countTransitions = 0;

    /**
     * Auxiliary class, It could be more easy if there are t-uples in java
     * represent the input of "function" sigma
     */
    public class Input {
        public String state;
        public Optional<Character> tapeItem;
        public Character stackItem;
        public Input(String st, Optional<Character> tI, Character sI) {
            state = st; tapeItem = tI; stackItem = sI;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Input input = (Input) o;

            if (!state.equals(input.state)) return false;
            if (!tapeItem.equals(input.tapeItem)) return false;
            return stackItem.equals(input.stackItem);
        }

        @Override
        public int hashCode() {
            int result = state.hashCode();
            result = 31 * result + tapeItem.hashCode();
            result = 31 * result + stackItem.hashCode();
            return result;
        }
    }

    /**
     * Represent the output of sigma "function"
     *
     * NOTE: it is like a tuple no need more abstraction over its attributes, no getters or setters.
     */
    public class Output {
        /**
         * Number of transition, only for tracer purposes
         */
        public int numTransition;

        public String state;

        public String stackItems;

        public Output(String st, String sIs, int nTransition) {
            state = st; stackItems = sIs; numTransition = nTransition;
        }
    }

    /**
     * Add elements
     */
    public void add(String state, Optional<Character> tapeChar, Character popStack, String toState, String pushStack) {

        Input input = new Input(state, tapeChar, popStack);
        Output output = new Output(toState, pushStack, countTransitions++);

        ArrayList<Output> outputs = Optional.ofNullable(getTransition().get(input))
                .map((outputs_) -> {
                    outputs_.add(output);
                    return outputs_;
                })
                .orElseGet(() -> {
                    ArrayList<Output> newArr = new ArrayList<>();
                    newArr.add(output);
                    return newArr;
                });

        getTransition().put(input, outputs);
    }

    /**
     * Return a "tuple"
     */
    public ArrayList<Output> apply(String state, Character tapeItem, Character stackItem) {
        return Optional.ofNullable(getTransition().get(new Input(state, Optional.of(tapeItem), stackItem)))
                .orElse(new ArrayList<>());
    }

    /**
     * Return a "tuple"
     */
    public ArrayList<Output> apply(String state, Character stackItem) {
        return Optional.ofNullable(getTransition().get(new Input(state, Optional.empty(), stackItem)))
                .orElse(new ArrayList<>());
    }


    private HashMap<Input, ArrayList<Output>> getTransition() {
        return transitions;
    }

    /**
     *
     */
    public HashMap<Input, ArrayList<Output>> getTransitions() {
        return transitions;
    }
}
