package com.lfa.automata.afd;

import java.util.HashSet;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.lfa.constants.Alphabet;
import com.lfa.constants.Alphabet.Symbol;
import com.lfa.constants.Constants;
import com.lfa.exception.ValidationException;

/**
 * Classe State. Representa um estado de um AFD.
 */
@Data
@EqualsAndHashCode(of = "name")
public class State {

	private final String name;
	private final HashSet<Transition> transitions;

	/**
	 * Instantiates a new state.
	 *
	 * @param name
	 *            the name
	 * @param finalState
	 *            the final state
	 */
	public State(String name) {
		this.name = name;
		this.transitions = new HashSet<>();
	}

	/**
	 * Adiciona uma transição de estado
	 *
	 * @param consumed
	 *            A string do símbolo consumido
	 * @param destination
	 *            O estado de destino
	 */
	public void addTransition(String consumed, State destination) {
		addTransition(Constants.ALPHABET.getSymbol(consumed), destination);
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
			throw new ValidationException("Destino ou símbolo nulo ou inexistente no alfabeto.");
		}
		if (consumed.getStr().equals(Alphabet.LAMBDA)) {
			throw new ValidationException("Não é possível adicionar transição que consome " + Alphabet.LAMBDA + " em um AFD.");
		}
		for (Transition transition : transitions) {
			if (transition.getConsumed().equals(consumed)) {
				throw new ValidationException("Já existe uma transição para o mesmo símbolo. Estado: " + this.getName() + ". Símbolo da transição: " + consumed.getStr() + ".");
			}
		}
		transitions.add(new Transition(this, consumed, destination));
	}
}
