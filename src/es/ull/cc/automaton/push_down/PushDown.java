/**
 * PushDownAutomaton - PushDown.java 23/09/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package es.ull.cc.automaton.push_down;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A push down automaton, parametrized to Alphabet of type `A` for input tape, and `Z` type alphabet of stack.
 * And alphabet it could be very large, therefore its used a string to represent a letter into it.
 */
public class PushDown {
    /**
     * Transitions, correspond to sigma element in the definition of Push Down Automaton
     */
    private HashMap<Input, Output> transitions;

    /**
     * Initial state to run transitions
     */
    private int initialState;
    /**
     * Initial Stack Item
     */
    private String initialStackItem;
    /**
     * A set of end states, to determine if that string belong to language
     */
    private BitSet endStates;

    /**
     * Tape Alphabet
     */
    private Set<String> tapeAlphabet;

    /**
     * Stack alphabet
     */
    private Set<String> stackAlphabet;

    /**
     * Auxiliary class, It could be more easy if there are t-uples in java
     * represent the input of "function" sigma
     */
    private class Input {
        public int state;
        public Character tapeItem;
        public Character stackItem;
    }

    /**
     * Represent the output of sigma "function"
     */
    private class Output {
        public int state;
        public Vector<Character> stackItems;
    }


    /**
     * Initialize from string a push down automaton
     * Following the next format:
     * # Commentary
     * q1 q2 q3 … # conjunto Q
     * a1 a2 a3 … # conjunto Σ
     * A1 A2 A3 … # conjunto Γ
     * q1 # estado inicial
     * A1 # símbolo inicial de la pila
     * q2 q3 # conjunto F
     * q1 a1 A1 q2 A2 # función de transición: δ (q1, a1, A1) = (q2, A2)
     *
     * TODO: THINK I should do a parser for this type of input? It has similar characteristics through problems
     * THINK: ignoreComments commentsParser >>> do PushDown <$> takeSets <*> takeAlphabet <*> ... <*> many transitions
     */
    public PushDown(String path) {
        try {
            // takeOne :: Line -> (Line -> IO)-> IO throw;
            BiFunction<Stream<String>, Consumer<Stream<String>>, Optional<String>> takeOne = (stream, func) ->
                stream.limit(1).findAny().map((str) -> { func.accept(Arrays.stream(str.split(" "))); return str; });

            // TODO: Improve this parser with idea
            Stream<String> stream = Files.lines(Paths.get(path));
            takeOne.apply(stream, (states) -> {
                states.map(Integer::parseInt)
                        .forEach((i) -> {}); // TODO: Guardar el conjunto de estados??
            });

            takeOne.apply(stream, (rawTapeAlphabet) ->
                    rawTapeAlphabet.forEach((letter) ->
                            tapeAlphabet.add(letter)))
                    .orElseThrow(() -> new IOException("Error in tape's alphabet"));

            takeOne.apply(stream, (rawStackAlphabet) ->
                    rawStackAlphabet.forEach((letter) ->
                            stackAlphabet.add(letter)))
                    .orElseThrow(() -> new IOException("Error in stack's alphabet"));

            takeOne.apply(stream, (rawInitialState) ->
                    rawInitialState.findAny().map((initState) ->
                        initialState = Integer.parseInt(initState)))
                    .orElseThrow(() -> new IOException("Error initial state"));


            takeOne.apply(stream, (rawInitialStack) ->
                    rawInitialStack.findAny().map((initState) ->
                            initialStackItem = initState))
                    .orElseThrow(() -> new IOException("Error initial stack item"));

            takeOne.apply(stream, (rawEndStates) ->
                    rawEndStates.forEach((letter) ->
                            endStates.set(Integer.parseInt(letter))))
                    .orElseThrow(() -> new IOException("Error end states set"));

            stream.map((line) -> {

            })

        }
        catch (IOException a) {

        }
    }

    /**
     *
     */
    public HashMap<Input, Output> getTransitions() {
        return transitions;
    }

    public PushDown setTransitions(HashMap<Input, Output> transitions) {
        this.transitions = transitions;
        return this;
    }

    /**
     *
     */
    public int getInitialState() {
        return initialState;
    }

    public PushDown setInitialState(int initialState) {
        this.initialState = initialState;
        return this;
    }

    /**
     *
     */
    public Character getInitialStackItem() {
        return initialStackItem;
    }

    public PushDown setInitialStackItem(Character initialStackItem) {
        this.initialStackItem = initialStackItem;
        return this;
    }

    /**
     *
     */
    public BitSet getEndStates() {
        return endStates;
    }

    public PushDown setEndStates(BitSet endStates) {
        this.endStates = endStates;
        return this;
    }

    /**
     *
     */
    public Set<String> getTapeAlphabet() {
        return tapeAlphabet;
    }

    public PushDown setTapeAlphabet(Set<String> tapeAlphabet) {
        this.tapeAlphabet = tapeAlphabet;
        return this;
    }

    /**
     *
     */
    public Set<String> getStackAlphabet() {
        return stackAlphabet;
    }

    public PushDown setStackAlphabet(Set<String> stackAlphabet) {
        this.stackAlphabet = stackAlphabet;
        return this;
    }


}
