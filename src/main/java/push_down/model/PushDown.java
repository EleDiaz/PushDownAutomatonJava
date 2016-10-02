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
     * q1 a1 A1 q2 A2 # TransitionState function: δ (q1, a1, A1) = (q2, A2)
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
                .forEach(state -> getStates().add(state));

        Arrays.stream(matcher.group("tAlphabet").split(" "))
                .forEach(letter -> getTapeAlphabet().add(letter.charAt(0)));

        Arrays.stream(matcher.group("sAlphabet").split(" "))
                .forEach(letter -> getStackAlphabet().add(letter.charAt(0)));

        setInitialState(matcher.group("iState"));

        setInitialStackItem(matcher.group("iStack").charAt(0));

        Arrays.stream(matcher.group("eStates").split(" "))
                .forEach(state -> getEndStates().add(state)); // TODO: Checkear que esta contenido en states

        Arrays.stream(matcher.group("transitions").split("\n")).forEach((transition) -> {
            String[] args = transition.split(" ");
            String state                = args[0];
            // Dollar is the empty char TODO: Should be variable
            Optional<Character> charOpt = args[1].equals("$") ? Optional.empty() : Optional.of(args[1].charAt(0));
            Character popStack          = args[2].charAt(0);
            String toState              = args[3];
            String pushStack            = args[4].equals("$") ? "" : args[4];

            getTransitions().add(state, charOpt, popStack, toState, pushStack);
        });
    }

    /**
     * Return a vector of transitions.
     * @param transitionState A TransitionState
     * @return next transitions from given transitionState
     */
    public ArrayList<TransitionState> epsilonTransitions(TransitionState transitionState) throws Exception {
        ArrayList<TransitionState> transitionsStack = new ArrayList<>();

        Character topCharStack = transitionState.getStack().pop().orElseThrow(() -> new Exception("Empty stack to early"));

        Optional.ofNullable(getTransitions().apply(transitionState.getCurrentState(), topCharStack))
                .ifPresent(outputs ->
                    transitionsStack.addAll(outputs.stream()
                        .map(output -> {
                            Stack stack = new Stack(transitionState.getStack()).push(output.stackItems);
                            return new TransitionState(output.state, transitionState.getTape(), stack, output.numTransition);
                        })
                        .collect(Collectors.toCollection(ArrayList::new))));

        return transitionsStack;
    }

    /**
     * TODO:
     * @param transitionState
     * @return
     * @throws Exception
     */
    public ArrayList<TransitionState> nonEpsilonTransitions(TransitionState transitionState) throws Exception {
        ArrayList<TransitionState> transitionsStack = new ArrayList<>();

        Character topCharStack = transitionState.getStack().pop().orElseThrow(() -> new Exception("Empty stack to early"));

        transitionState.getTape().take().ifPresent((character) ->
            Optional.ofNullable(getTransitions().apply(transitionState.getCurrentState(), character, topCharStack))
                .ifPresent(outputs ->
                    transitionsStack.addAll(outputs.stream()
                        .map(output -> {
                            Stack stack = new Stack(transitionState.getStack().push(output.stackItems));
                            return new TransitionState(output.state, transitionState.getTape(), stack, output.numTransition);
                        })
                        .collect(Collectors.toCollection(ArrayList::new)))));
        return transitionsStack;
    }

    /**
     * Check if a string belong a determine push down automaton language
     * @param text
     * @return
     */
    public boolean checkString(String text, ArrayList<Breadcrumb> breadcrumbs) throws Exception {
        ArrayList<TransitionState> transitionsStack = new ArrayList<>();
        int countTransitions = 0;

        transitionsStack.add(new TransitionState(getInitialState(), new Tape(text), new Stack(getInitialStackItem())));

        boolean belong = false;

        while (!transitionsStack.isEmpty() && !belong) {
            TransitionState transitionState = transitionsStack.get(transitionsStack.size() - 1);
            transitionsStack.remove(transitionsStack.size() - 1);

            if (belongToLanguage(transitionState)) {
                belong = true;
                breadcrumbs.add(new Breadcrumb(transitionState, new BitSet(), countTransitions++, 0));
            }
            else if (!transitionState.getStack().isEmpty()) {
                ArrayList<TransitionState> aux = new ArrayList<>();
                aux.addAll(epsilonTransitions(new TransitionState(transitionState)));
                aux.addAll(nonEpsilonTransitions(new TransitionState(transitionState)));

                BitSet actions = new BitSet();
                aux.forEach(transition -> actions.set(transition.getIdTransition().get()));
                breadcrumbs.add(new Breadcrumb(
                      transitionState
                    , actions
                    , countTransitions++
                    , 0));

                transitionsStack.addAll(aux);
            }
        }
        return belong;
    }

    /**
     * Checks stack, tape and end states. To determine if belong
     * @param transitionState
     * @return
     */
    private boolean belongToLanguage(TransitionState transitionState) {
        return (transitionState.getTape().isEmpty() && transitionState.getStack().isEmpty())
                ||
                (transitionState.getTape().isEmpty() && getEndStates().contains(transitionState.getCurrentState()));
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
