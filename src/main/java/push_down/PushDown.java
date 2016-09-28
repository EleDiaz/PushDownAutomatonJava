/**
 * PushDownAutomaton - PushDown.java 23/09/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.*;

/**
 * A push down automaton, parametrized to Alphabet of type `A` for input tape, and `Z` type alphabet of stack.
 * And alphabet it could be very large, therefore its used a string to represent a letter into it.
 */
public class PushDown {
    /**
     * Transitions, correspond to sigma element in the definition of Push Down Automaton
     */
    private HashMap<Input, ArrayList<Output>> transitions;

    /**
     * Current set of states
     */
    private BitSet states = new BitSet();

    /**
     * Initial state to run transitions
     */
    private int initialState;
    /**
     * Initial Stack Item
     */
    private Character initialStackItem;

    /**
     * A set of end states, to determine if that string belong to language
     */
    private BitSet endStates;

    /**
     * Tape Alphabet
     */
    private Set<Character> tapeAlphabet;

    /**
     * Stack alphabet
     */
    private Set<Character> stackAlphabet;

    /**
     * Auxiliary class, It could be more easy if there are t-uples in java
     * represent the input of "function" sigma
     */
    public class Input {
        public int state;
        public Optional<Character> tapeItem;
        public Character stackItem;
        public Input(int st, Optional<Character> tI, Character sI) {
            state = st; tapeItem = tI; stackItem = sI;
        }
    }

    /**
     * Represent the output of sigma "function"
     */
    public class Output {
        public int state;
        public String stackItems;
        public Output(int st, String sIs) {
            state = st; stackItems = sIs;
        }
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
            Pattern p = Pattern.compile(
                    "(?<states>(\\d+ *)+)"
                    + "(?: *(#.+)?\\n)"
                    + "(?<tAlphabet>(. *)+)"
                    + "(?: *(#.+)?\\n)"
                    + "(?<sAlphabet>(. *)+)"
                    + "(?: *(#.+)?\\n)"
                    + "(?<iState>(\\d+))"
                    + "(?: *(#.+)?\\n)"
                    + "(?<iStack>.)"
                    + "(?: *(#.+)?\\n)"
                    + "(?<eStates>(\\d+ *)+)"
                    + "(?: *(#.+)?\\n)"
                    + "(?<transitions>(\\d+ +. +. +\\d+ + \\w +\\n)*)" // TODO: Comments in transition no its easily possible
            );
            Matcher matcher = p.matcher(new String(Files.readAllBytes(Paths.get(path))));

            Arrays.stream(matcher.group("states").split(" "))
                    .forEach((state) -> getStates().set(Integer.parseInt(state)));

            Arrays.stream(matcher.group("tAlphabet").split(" "))
                    .forEach((letter) -> getTapeAlphabet().add(letter.charAt(0)));

            Arrays.stream(matcher.group("sAlphabet").split(" "))
                    .forEach((letter) -> getStackAlphabet().add(letter.charAt(0)));

            setInitialState(Integer.parseInt(matcher.group("iState")));

            setInitialStackItem(matcher.group("iStack").charAt(0));

            Arrays.stream(matcher.group("eStates").split(" "))
                    .forEach((state) -> getEndStates().set(Integer.parseInt(state))); // TODO: Checkear que esta contenido en states

            Arrays.stream(matcher.group("transitions").split("\n")).forEach((transition) -> {
                String[] args = transition.split(" ");
                int state                   = Integer.parseInt(args[0]);

                // Dollar is the empty language TODO: Should be variable
                Optional<Character> charOpt = args[1].equals("$") ? Optional.empty() : Optional.of(args[1].charAt(0));
                Character popStack          = args[2].charAt(0);
                int toState                 = Integer.parseInt(args[3]);
                String pushStack            = args[4];

                Input input = new Input(state, charOpt, popStack);
                Output output = new Output(toState, pushStack);

                ArrayList<Output> outputs = Optional.ofNullable(getTransitions().get(input))
                        .map((outputs_) -> {
                            outputs_.add(output);
                            return outputs_;
                        })
                        .orElseGet(() -> {
                            ArrayList<Output> newArr = new ArrayList<Output>();
                            newArr.add(output);
                            return newArr;
                        });

                getTransitions().put(input, outputs);
            });
        }
        catch (IOException a) {
            System.out.println(a.getStackTrace().toString());
        }
    }

    /**
     * Return a vector of transitions.
     * @param transition
     * @return
     */
    public ArrayList<Transition> makeTransitions(Transition transition) {
        ArrayList<Transition> transitions = new ArrayList<>();

        // Pop last element from stack always
        Character lastStackChar = transition.popStack();
        String tape = transition.getTape();
        int cState  = transition.getCurrentState();
        ArrayList<Character> stack = transition.getStack();

        // Get transitions consuming a character
        if (!tape.isEmpty()) {
            Optional.ofNullable(getTransitions().get(new Input(cState, Optional.of(tape.charAt(0)), lastStackChar)))
                    .ifPresent((output) ->
                            transitions.addAll(Transition.make(output, tape.substring(1), stack))
                    );
        }

        // Without consuming a character
        Optional.ofNullable(getTransitions().get(new Input(cState, Optional.empty(), lastStackChar)))
                .ifPresent((output) -> transitions.addAll(Transition.make(output, tape, stack)));

        return transitions;
    }

    /**
     * Check if a string belong a determine push down automaton language
     * @param text
     * @return
     */
    public boolean checkString(String text) {
        ArrayList<Transition> transitions = new ArrayList<>();

        //// TODO: This is repetition of above. That it's a problem
        if (!text.isEmpty()) {
            Optional.ofNullable(getTransitions().get(new Input(getInitialState(), Optional.of(text.charAt(0)), getInitialStackItem())))
                    .ifPresent((output) ->
                            transitions.addAll(Transition.make(output, text.substring(1), new ArrayList<>())));
        }

        // Without consuming a character
        Optional.ofNullable(getTransitions().get(new Input(getInitialState(), Optional.empty(), getInitialStackItem())))
                .ifPresent((output) ->
                        transitions.addAll(Transition.make(output, text, new ArrayList<>())));

        boolean belong = false;

        while (!transitions.isEmpty() && !belong) {
            Transition transition = transitions.get(transitions.size() - 1);
            transitions.remove(transitions.size() - 1);

            if (belongToLanguage(transition)) {
                belong = true;
            }
            else {
                transitions.addAll(makeTransitions(transition));
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
                (transition.getTape().isEmpty() && getEndStates().get(transition.getCurrentState()))
    }

    //// Getters and Setters

    public PushDown setInitialState(int initialState) {
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
    public BitSet getStates() {
        return states;
    }

    /**
     *
     */
    public HashMap<Input, ArrayList<Output>> getTransitions() {
        return transitions;
    }

    /**
     *
     */
    public int getInitialState() {
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
    public BitSet getEndStates() {
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
