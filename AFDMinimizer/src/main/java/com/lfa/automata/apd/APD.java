package com.lfa.automata.apd;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableSet;
import com.lfa.automata.Automata;

/**
 * A classe APD. Representa um APD.
 */
@Data
public class APD implements Automata {

	private final APDState initialState;
	private final ImmutableSet<APDState> states;
	private final ImmutableSet<APDState> finalStates;
	private final Deque<String> stack;

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
		this.stack = new ArrayDeque<>();
	}

	@Override
	public void run(String str) {
		APDState present = initialState;
		while (!(finalStates.contains(present) && stack.isEmpty() && StringUtils.isEmpty(str))) {
			str = present.consume(str, stack);
			present = present.getNext();
		}
	}
}
