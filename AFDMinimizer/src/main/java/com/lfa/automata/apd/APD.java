package com.lfa.automata.apd;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import com.lfa.automata.Automata;
import com.lfa.collections.ImmutableLinkedSet;

/**
 * A classe APD. Representa um APD.
 */
@Data
@EqualsAndHashCode(callSuper = false, of = { "initialState", "states", "finalStates" })
@ToString(callSuper = false, of = { "initialState", "states", "finalStates", "stack" })
public class APD implements Automata {

	private final APDState initialState;
	private final ImmutableLinkedSet<APDState> states;
	private final ImmutableLinkedSet<APDState> finalStates;
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
		this.states = ImmutableLinkedSet.<APDState> builder().addAll(states).build();
		this.finalStates = ImmutableLinkedSet.<APDState> builder().addAll(finalStates).build();
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

	@Override
	public int size() {
		return states.size();
	}
}
