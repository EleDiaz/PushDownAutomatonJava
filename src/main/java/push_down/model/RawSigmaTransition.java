/**
 * PushDownAutomaton - RawSigmaTransition.java 2/10/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.push_down.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.Optional;

/**
 * TODO: Commenta algo
 *
 */
public class RawSigmaTransition {
    private SimpleIntegerProperty numTransition;
    private SimpleStringProperty transitionStr;

    private RawSigmaTransition(int nTransition, String st, Optional<Character> cInput, Character sInput, String toSt, String pStack) {
        numTransition = new SimpleIntegerProperty(nTransition);
        transitionStr = new SimpleStringProperty(
            "𝛿 "
            + st
            + " "
            + cInput.orElse('ø').toString()
            + " "
            + sInput.toString()
            + " = ("
            + toSt
            + ", "
            + (pStack.isEmpty() ? "ø": pStack)
            + ")"
        );
    }

    static public ArrayList<RawSigmaTransition> getRawTransitions(FuncTransition funcTransition) {
        ArrayList<RawSigmaTransition> result = new ArrayList<>();

        funcTransition
            .getTransitions()
            .entrySet()
            .forEach((entry) ->
                entry
                    .getValue()
                    .forEach((output) ->
                        result.add(new RawSigmaTransition(
                            output.numTransition
                            , entry.getKey().state
                            , entry.getKey().tapeItem
                            , entry.getKey().stackItem
                            , output.state
                            , output.stackItems))
                    )
            );

        return result;
    }

    public int getNumTransition() {
        return numTransition.get();
    }

    public SimpleIntegerProperty numTransitionProperty() {
        return numTransition;
    }

    public void setNumTransition(int numTransition) {
        this.numTransition.set(numTransition);
    }

    public String getTransitionStr() {
        return transitionStr.get();
    }

    public SimpleStringProperty transitionStrProperty() {
        return transitionStr;
    }

    public void setTransitionStr(String transitionStr) {
        this.transitionStr.set(transitionStr);
    }
}
