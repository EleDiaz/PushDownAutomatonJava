/*
 * PushDownAutomaton - PushDown.java 23/09/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A push down automaton, parametrized to Alphabet of type `A` for input tape, and `Z` type alphabet of stack.
 * And alphabet it could be very large, therefore its used a string to represent a letter into it.
 */
public class PushDown {
    /**
     * Transitions, correspond to sigma element in the definition of Push Down Automaton
     */
    private FuncTransition transitions = new FuncTransition();

    /**
     * Current set of states
     */
    private HashSet<String> states = new HashSet<>();

    /**
     * Initial state to run transitions
     */
    private String initialState;

    /**
     * Initial Stack Item
     */
    private Character initialStackItem;

    /**
     * A set of end states, to determine if that string belong to language
     */
    private HashSet<String> endStates = new HashSet<>();

    /**
     * Tape Alphabet
     */
    private HashSet<Character> tapeAlphabet = new HashSet<>();

    /**
     * Stack alphabet
     */
    private HashSet<Character> stackAlphabet = new HashSet<>();

    /**
     * Initialize from string a push down automaton
     * Following the next format:
     * # Commentary
     * q1 q2 q3 … # Set Q it could be "q1", "1", "p"
     * a1 a2 a3 … # Set Σ limit to a char only
     * A1 A2 A3 … # Set Γ limit to a char only
     * q1 # Initial State
     * A1 # Initial state of symbol
     * q2 q3 # Set F
     * q1 a1 A1 q2 A2 # Transition function: δ (q1, a1, A1) = (q2, A2)
     *
     * THINK: ignoreComments commentsParser >>> do PushDown <$> takeSets <*> takeAlphabet <*> ... <*> many transitions
     */
    public PushDown(String path) throws Exception {
        Pattern p = Pattern.compile(
                "( *(#.+)?\\n)*"
                + "(?<states> *\\w+( +\\w+)*)"   // States
                + " *(#.+)?\\n"
                + "(?<tAlphabet> *\\w( +\\w)*)"
                + " *(#.+)?\\n"
                + "(?<sAlphabet> *\\w( +\\w)*)"
                + " *(#.+)?\\n"
                + "(?<iState> *\\w+)"
                + " *(#.+)?\\n"
                + "(?<iStack> *\\w)"
                + " *(#.+)?\\n"
                + "(?<eStates>\\w?( *\\w)*)"
                + " *(#.+)?\\n"
                + "(?<transitions>( *\\w+ +[$\\w] +\\w +\\w+ +[$\\w]+ *\\n)*)" // TODO: Comments in transition no its easily possible
                + "[ +\\n]*"
        );
        Matcher matcher = p.matcher(new String(Files.readAllBytes(Paths.get(path))));
        if (!matcher.matches()) {
            throw new Exception("Fail to parse file. See the format specified");
        }

        Arrays.stream(matcher.group("states").split(" "))
                .forEach((state) -> getStates().add(state));

        Arrays.stream(matcher.group("tAlphabet").split(" "))
                .forEach((letter) -> getTapeAlphabet().add(letter.charAt(0)));

        Arrays.stream(matcher.group("sAlphabet").split(" "))
                .forEach((letter) -> getStackAlphabet().add(letter.charAt(0)));

        setInitialState(matcher.group("iState"));

        setInitialStackItem(matcher.group("iStack").charAt(0));

        Arrays.stream(matcher.group("eStates").split(" "))
                .forEach((state) -> getEndStates().add(state)); // TODO: Checkear que esta contenido en states

        Arrays.stream(matcher.group("transitions").split("\n")).forEach((transition) -> {
            String[] args = transition.split(" ");
            String state                   = args[0];

            // Dollar is the empty language TODO: Should be variable
            Optional<Character> charOpt = args[1].equals("$") ? Optional.empty() : Optional.of(args[1].charAt(0));
            Character popStack          = args[2].charAt(0);
            String toState              = args[3];
            String pushStack            = args[4].equals("$") ? "" : args[4];

            getTransitions().add(state, charOpt, popStack, toState, pushStack);
        });
    }

    /**
     * Return a vector of transitions.
     * @param transition A Transition
     * @return next transitions from given transition
     */
    public ArrayList<Transition> epsilonTransitions(Transition transition) throws Exception {
        ArrayList<Transition> transitionsStack = new ArrayList<>();

        Character topCharStack = transition.getStack().pop().orElseThrow(() -> new Exception("Empty stack to early"));

        Optional.ofNullable(getTransitions().apply(transition.getCurrentState(), topCharStack))
                .ifPresent((outputs) ->
                    transitionsStack.addAll(outputs.stream()
                        .map((output) -> {
                            Stack stack = new Stack(transition.getStack()).push(output.stackItems);
                            return new Transition(output.state, transition.getTape(), stack);
                        })
                        .collect(Collectors.toCollection(ArrayList::new))));

        return transitionsStack;
    }

    /**
     * TODO:
     * @param transition
     * @return
     * @throws Exception
     */
    public ArrayList<Transition> nonEpsilonTransitions(Transition transition) throws Exception {
        ArrayList<Transition> transitionsStack = new ArrayList<>();

        Character topCharStack = transition.getStack().pop().orElseThrow(() -> new Exception("Empty stack to early"));

        transition.getTape().take().ifPresent((character) ->
            Optional.ofNullable(getTransitions().apply(transition.getCurrentState(), character, topCharStack))
                .ifPresent((outputs) ->
                    transitionsStack.addAll(outputs.stream()
                        .map((output) -> {
                            Stack stack = new Stack(transition.getStack().push(output.stackItems));
                            return new Transition(output.state, transition.getTape(), stack);
                        })
                        .collect(Collectors.toCollection(ArrayList::new)))));
        return transitionsStack;
    }

    /**
     * Check if a string belong a determine push down automaton language
     * @param text
     * @return
     */
    public boolean checkString(String text) throws Exception {
        ArrayList<Transition> transitionsStack = new ArrayList<>();

        transitionsStack.add(new Transition(getInitialState(), new Tape(text), new Stack(getInitialStackItem())));

        boolean belong = false;

        while (!transitionsStack.isEmpty() && !belong) {
            Transition transition = transitionsStack.get(transitionsStack.size() - 1);
            transitionsStack.remove(transitionsStack.size() - 1);

            if (belongToLanguage(transition)) {
                belong = true;
            }
            else if (!transition.getStack().isEmpty()) {
                transitionsStack.addAll(epsilonTransitions(new Transition(transition)));
                transitionsStack.addAll(nonEpsilonTransitions(new Transition(transition)));
            }
        }
        return belong;
    }

    /**
     * Checks stack, tape and end states. To determine if belong
     * @param transition
     * @return
     */
    private boolean belongToLanguage(Transition transition) {
        return (transition.getTape().isEmpty() && transition.getStack().isEmpty())
                ||
                (transition.getTape().isEmpty() && getEndStates().contains(transition.getCurrentState()));
    }

    //// Getters and Setters

    public PushDown setInitialState(String initialState) {
        this.initialState = initialState;
        return this;
    }

    public PushDown setInitialStackItem(Character initialStackItem) {
        this.initialStackItem = initialStackItem;
        return this;
    }

    /**
     *
     */
    public HashSet<String> getStates() {
        return states;
    }

    /**
     *
     */
    public FuncTransition getTransitions() {
        return transitions;
    }

    /**
     *
     */
    public String getInitialState() {
        return initialState;
    }

    /**
     *
     */
    public Character getInitialStackItem() {
        return initialStackItem;
    }

    /**
     *
     */
    public HashSet<String> getEndStates() {
        return endStates;
    }

    /**
     *
     */
    public Set<Character> getTapeAlphabet() {
        return tapeAlphabet;
    }

    /**
     *
     */
    public Set<Character> getStackAlphabet() {
        return stackAlphabet;
    }
}
