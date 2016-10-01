/**
 * PushDownAutomaton - Stack.java 30/09/16
 *
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down.model;

import java.util.Optional;

/**
 * A wrapper around string type to limit expose interface to look a stack of chars
 *
 */
public class Stack {
    String stack;

    public Stack(Stack stack) {
        this.stack = stack.getStack();
    }

    public Stack(Character stackItem) {
        stack = stackItem.toString();
    }

    /**
     * A safe pop operation over a stack
     * @return can return a character
     */
    public Optional<Character> pop() {
        if (stack.isEmpty()) {
            return Optional.empty();
        } else {
            char aux = stack.charAt(0);
            stack = stack.substring(1);
            return Optional.of(aux);
        }
    }

    /**
     * Add elements to stack
     * @param elements each character is a element
     */
    public Stack push(String elements) {
        stack = elements + stack;
        return this;
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }



    /**
     *
     */
    public String getStack() {
        return stack;
    }
}
