package com.lfa.minimize;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.lfa.automata.afd.State;
import com.lfa.constants.Symbol;

/**
 * Classe Group. É a representação de um grupo de minimização. Assim como o
 * grupo no algoritmo, não é possível adicionar estados a um grupo, apenas
 * remover.
 */
@Data
@EqualsAndHashCode(callSuper = false, of = "name")
@ToString(callSuper = false, of = { "name", "currentStates", "groupTransitions" })
public class Group {

	private static int NUM_GROUPS = 0;

	private final String name;

	@Getter(value = AccessLevel.NONE)
	private final HashSet<State> currentStates;
	private HashMap<Symbol, Group> groupTransitions;

	/**
	 * Instancia um novo grupo atribuindo um nome automaticamente, com base no
	 * número de grupos criados.
	 *
	 * @param states
	 *            Os estados do grupo
	 */
	protected Group(Collection<State> states) {
		this("G" + (++NUM_GROUPS), states);
	}

	/**
	 * Instancia um novo grupo.
	 *
	 * @param name
	 *            O nome do grupo
	 * @param states
	 *            Os estados do grupo
	 */
	protected Group(String name, Collection<State> states) {
		this.currentStates = new HashSet<>(states);
		this.name = name;
	}

	/**
	 * Remove deste grupo os estados na coleção fornecida.
	 *
	 * @param states
	 *            Os estados
	 */
	public void remove(Collection<State> states) {
		states.removeAll(states);
	}

	/**
	 * Remove deste grupo os estados na coleção fornecida.
	 *
	 * @param states
	 *            Os estados
	 */
	public void remove(State... states) {
		for (State state : states) {
			currentStates.remove(state);
		}
	}

	/**
	 * Verifica se um estado pertence ao grupo.
	 *
	 * @param state
	 *            O estado.
	 * @return true, se pertencer ao grupo.
	 */
	public boolean contains(State state) {
		return currentStates.contains(state);
	}

	/**
	 * Retorna o primeiro estado do conjunto de estados do grupo
	 *
	 * @return O primeiro estado.
	 */
	public State getFirst() {
		return currentStates.iterator().next();
	}

}
