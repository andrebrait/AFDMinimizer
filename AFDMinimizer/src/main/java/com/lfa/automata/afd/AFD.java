package com.lfa.automata.afd;

import java.util.Collection;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableSet;
import com.lfa.automata.Automata;

@Data
public class AFD implements Automata {

	private final State initialState;
	private final ImmutableSet<State> states;
	private final ImmutableSet<State> finalStates;

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
