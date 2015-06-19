package com.lfa.automata.afd;

import java.util.LinkedHashSet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import com.lfa.constants.Alphabet;
import com.lfa.constants.Alphabet.Symbol;
import com.lfa.constants.Constants;
import com.lfa.exception.ValidationException;
import com.lfa.exception.ValidationException.ErrorType;

/**
 * Classe State. Representa um estado de um AFD.
 *
 * @author Andre Brait (andrebrait@gmail.com)
 */
@Data
@EqualsAndHashCode(callSuper = false, of = "name")
@ToString(callSuper = false, of = "name")
public class State {

	/**
	 * Classe Transition. Representa uma transição de estado de um AFD.
	 */
	@Data
	@EqualsAndHashCode(callSuper = false, of = { "source", "consumed", "destination" })
	@ToString(callSuper = false, of = { "source", "consumed", "destination" })
	public static class Transition {

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

	private final String name;
	private final LinkedHashSet<Transition> transitions;

	/**
	 * O próximo estado, sendo ele o estado de destino da última transição
	 * percorrida.
	 */
	private State next;

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
		this.transitions = new LinkedHashSet<>();
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
			throw new ValidationException(ErrorType.AFD, "Destino ou símbolo nulo ou inexistente no alfabeto.");
		}
		if (consumed.getStr().equals(Alphabet.LAMBDA)) {
			throw new ValidationException(ErrorType.AFD, "Não é possível adicionar transição que consome " + Alphabet.LAMBDA + " em um AFD.");
		}
		for (Transition transition : transitions) {
			if (transition.getConsumed().equals(consumed)) {
				throw new ValidationException(ErrorType.AFD, "Já existe uma transição para o mesmo símbolo." + Constants.NEWLINE + "Estado: " + this.getName() + ". Símbolo da transição: "
						+ consumed.getStr() + ".");
			}
		}
		transitions.add(new Transition(this, consumed, destination));
	}

	/**
	 * Adiciona uma transição invertida. Na verdade, apenas adiciona uma
	 * transição sem que se faça quaisquer checagens de consistência. Deve ser
	 * usado SOMENTE pelo gerador de palavras.
	 *
	 * @param consumed
	 *            O símbolo consumido.
	 * @param destination
	 *            O estado de destino.
	 */
	public void addReverseTransition(Symbol consumed, State destination) {
		transitions.add(new Transition(this, consumed, destination));
	}

	/**
	 * Consome o primeiro símbolo da string e passa ao próximo estado.
	 *
	 * @param str
	 *            A string
	 * @return A string original menos o caractere consumido.
	 */
	public String consume(String str) {
		String firstChar = StringUtils.left(str, 1);
		for (Transition trans : getTransitions()) {
			if (trans.getConsumed().getStr().equals(firstChar)) {
				setNext(trans.getDestination());
				return StringUtils.removeStart(str, firstChar);
			}
		}
		throw new ValidationException(ErrorType.AFD, "Transição não encontrada para o símbolo presente no estado " + getName() + "." + Constants.NEWLINE + "Símbolo: " + firstChar + ". String: " + str
				+ ".");
	}
}
