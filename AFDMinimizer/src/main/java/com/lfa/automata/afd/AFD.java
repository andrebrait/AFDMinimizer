package com.lfa.automata.afd;

import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableSet;
import com.lfa.automata.Automata;

/**
 * A classe AFD. Representa um AFD no sistema. É capaz de reconhecer palavras
 * através do método run.
 */
@Data
@EqualsAndHashCode(callSuper = false, of = { "initialState", "states", "finalStates" })
@ToString(callSuper = false, of = { "initialState", "states", "finalStates" })
public class AFD implements Automata {

	private final State initialState;
	private final ImmutableSet<State> states;
	private final ImmutableSet<State> finalStates;

	/**
	 * Instancia um novo AFD.
	 *
	 * @param initialState
	 *            O estado inicial do AFD.
	 * @param states
	 *            A coleção de estados do AFD, incluindo o inicial e o final.
	 * @param finalStates
	 *            A coleção de estados finais do AFD.
	 */
	public AFD(State initialState, Collection<State> states, Collection<State> finalStates) {
		this.initialState = initialState;
		this.states = ImmutableSet.<State> builder().addAll(states).build();
		this.finalStates = ImmutableSet.<State> builder().addAll(finalStates).build();
	}

	@Override
	public void run(String str) {
		State present = initialState;
		while (!(finalStates.contains(present) && StringUtils.isEmpty(str))) {
			str = present.consume(str);
			present = present.getNext();
		}
	}

}
