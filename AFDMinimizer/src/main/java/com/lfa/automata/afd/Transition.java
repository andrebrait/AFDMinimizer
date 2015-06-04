package com.lfa.automata.afd;

import lombok.Data;

import com.lfa.constants.Alphabet.Symbol;

@Data
public class Transition {

	private final Symbol consumed;
	private final State source;
	private final State destination;

	/**
	 * Instancia uma nova transição.
	 *
	 * @param source
	 *            O estado de origem.
	 * @param consumed
	 *            O símbolo consumido.
	 * @param destination
	 *            O estado de destino.
	 */
	protected Transition(State source, Symbol consumed, State destination) {
		this.source = source;
		this.consumed = consumed;
		this.destination = destination;
	}

}
