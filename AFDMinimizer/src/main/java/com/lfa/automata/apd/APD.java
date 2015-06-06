package com.lfa.automata.apd;

import java.util.Collection;
import java.util.Stack;

import lombok.Data;

import com.google.common.collect.ImmutableSet;

/**
 * A classe APD. Representa um APD.
 */
@Data
public class APD {

	private final APDState initialState;
	private final ImmutableSet<APDState> states;
	private final ImmutableSet<APDState> finalStates;
	private final Stack<String> stack;

	/**
	 * Instancia um novo APD.
	 *
	 * @param initialState
	 *            O estado inicial.
	 * @param states
	 *            Os estados.
	 * @param finalStates
	 *            os estados finais.
	 */
	public APD(APDState initialState, Collection<APDState> states, Collection<APDState> finalStates) {
		this.initialState = initialState;
		this.states = ImmutableSet.<APDState> builder().addAll(states).build();
		this.finalStates = ImmutableSet.<APDState> builder().addAll(finalStates).build();
		this.stack = new Stack<>();
	}

	public void run(String str) {

	}

}
