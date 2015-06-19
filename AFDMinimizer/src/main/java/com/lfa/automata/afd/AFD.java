package com.lfa.automata.afd;

import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import com.lfa.automata.Automata;
import com.lfa.collections.ImmutableLinkedSet;

/**
 * A classe AFD. Representa um AFD no sistema. É capaz de reconhecer palavras
 * através do método run.
 *
 * @author Andre Brait (andrebrait@gmail.com)
 */
@Data
@EqualsAndHashCode(callSuper = false, of = { "initialState", "states", "finalStates" })
@ToString(callSuper = false, of = { "initialState", "states", "finalStates" })
public class AFD implements Automata {

	private final State initialState;
	private final ImmutableLinkedSet<State> states;
	private final ImmutableLinkedSet<State> finalStates;

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
		this.states = ImmutableLinkedSet.<State> builder().addAll(states).build();
		this.finalStates = ImmutableLinkedSet.<State> builder().addAll(finalStates).build();
	}

	@Override
	public void run(String str) {
		State present = initialState;
		while (!(finalStates.contains(present) && StringUtils.isEmpty(str))) {
			str = present.consume(str);
			present = present.getNext();
		}
	}

	@Override
	public int size() {
		return states.size();
	}

}