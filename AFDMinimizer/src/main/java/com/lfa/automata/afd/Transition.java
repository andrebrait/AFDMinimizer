package com.lfa.automata.afd;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.lfa.constants.Symbol;

@Data
@EqualsAndHashCode(callSuper = false, of = { "source", "consumed", "destination" })
@ToString(callSuper = false, of = { "source", "consumed", "destination" })
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
