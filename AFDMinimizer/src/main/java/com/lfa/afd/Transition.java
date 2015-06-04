package com.lfa.afd;

import lombok.Data;

import com.lfa.constants.Alphabet.Symbol;
import com.lfa.constants.Constants;

@Data
public class Transition {

	private final Symbol consumed;
	private final State destination;

	/**
	 * Instantiates a new transition.
	 *
	 * @param consumed
	 *            the consumed
	 * @param destination
	 *            the destination
	 */
	public Transition(Symbol consumed, State destination) {
		this.consumed = consumed;
		this.destination = destination;
	}

	/**
	 * Instantiates a new transition.
	 *
	 * @param consumed
	 *            the consumed
	 * @param destination
	 *            the destination
	 */
	public Transition(String consumed, State destination) {
		this(Constants.ALPHABET.getSymbol(consumed), destination);
	}
}
