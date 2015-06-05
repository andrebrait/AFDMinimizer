package com.lfa.automata.apd;

import java.util.Collection;

import lombok.Data;

import com.google.common.collect.ImmutableSet;

@Data
public class APD {

	private final APDState initialState;
	private final ImmutableSet<APDState> states;
	private final ImmutableSet<APDState> finalStates;

	public APD(APDState initialState, Collection<APDState> states, Collection<APDState> finalStates) {
		this.initialState = initialState;
		this.states = ImmutableSet.<APDState> builder().addAll(states).build();
		this.finalStates = ImmutableSet.<APDState> builder().addAll(finalStates).build();
	}

}
