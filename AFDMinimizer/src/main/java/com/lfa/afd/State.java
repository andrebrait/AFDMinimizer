package com.lfa.afd;

import java.util.HashSet;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.lfa.constants.Alphabet;
import com.lfa.constants.Alphabet.Symbol;
import com.lfa.exception.ValidationException;

/**
 * Classe State. Representa um estado de um AFD.
 */
@Data
@EqualsAndHashCode(of = "name")
public class State {

	private final String name;
	private final boolean finalState;
	private final HashSet<Transition> transitions;

	/**
	 * Instantiates a new state.
	 *
	 * @param name
	 *            the name
	 */
	public State(String name) {
		this(name, false);
	}

	/**
	 * Instantiates a new state.
	 *
	 * @param name
	 *            the name
	 * @param finalState
	 *            the final state
	 */
	public State(String name, boolean finalState) {
		this.name = name;
		this.finalState = finalState;
		this.transitions = new HashSet<>();
	}

	/**
	 * Adiciona uma transição de estado
	 *
	 * @param consumed
	 *            O símbolo consumido
	 * @param destination
	 *            O estado de destino
	 */
	public void addTransition(Symbol consumed, State destination) {
		if (consumed == null || destination == null) {
			throw new ValidationException("Símbolo consumido ou destino nulos");
		}
		if (consumed.getStrOrLambda().equals(Alphabet.LAMBDA)) {
			throw new ValidationException("Não é possível adicionar transição que consome " + Alphabet.LAMBDA + " em um AFD.");
		}
		for (Transition transition : transitions) {
			if (transition.getConsumed().equals(consumed)) {
				throw new ValidationException("Já existe uma transição para o mesmo símbolo. Estado: " + this.getName() + ". Símbolo da transição: " + consumed.getStr() + ".");
			}
		}
		transitions.add(new Transition(consumed, destination));
	}
}
