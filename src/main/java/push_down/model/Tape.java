/**
 * PushDownAutomaton - Tape.java 30/09/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down.model;

import java.util.Optional;

/**
 * A wrapper to consumer tape
 *
 * NOTE: La capacidad de ruby de abrir las clases vendria mejor
 */
public class Tape {
    private String tape;

    public Tape(Tape tape) {
        this.tape = tape.getTape();
    }

    public Tape(String tape) {
        this.tape = tape;
    }

    public Optional<Character> take() {
        if (tape.isEmpty()) {
            return Optional.empty();
        } else {
            char aux = tape.charAt(0);
            tape = tape.substring(1);
            return Optional.of(aux);
        }
    }

    public boolean isEmpty() {
        return tape.isEmpty();
    }

    /**
     *
     */
    public String getTape() {
        return tape;
    }

}
