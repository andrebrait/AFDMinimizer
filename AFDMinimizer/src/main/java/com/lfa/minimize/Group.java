package com.lfa.minimize;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.google.common.collect.ImmutableSet;
import com.lfa.automata.afd.AFD;
import com.lfa.automata.afd.State;
import com.lfa.automata.afd.State.Transition;
import com.lfa.constants.Alphabet.Symbol;
import com.lfa.constants.Constants;
import com.lfa.exception.ValidationException;
import com.lfa.exception.ValidationException.ErrorType;

/**
 * Classe Group. É a representação de um grupo de minimização. Assim como o
 * grupo no algoritmo, não é possível adicionar estados a um grupo, apenas
 * remover. Apesar de muitas similaridades com {@link State}, a extensão não é
 * viável dada a diferença semântica entre ambas.
 */
@Data
@EqualsAndHashCode(callSuper = false, of = "name")
@ToString(callSuper = false, of = { "name", "states", "groupTransitions" })
public final class Group {

	/**
	 * Classe GroupTransition. Representa a transição de um {@link Group} para o
	 * outro. É propositalmente análoga à classe {@link Transition} para tornar
	 * fácil a conversão de grupos em estados de um {@link AFD} minimizado.
	 */
	@Data
	@EqualsAndHashCode(callSuper = false, of = { "consumed", "destination" })
	@ToString(callSuper = false, of = { "consumed", "destination" })
	public final static class GroupTransition {
		private final Symbol consumed;
		private final Group destination;

		/**
		 * Instancia uma nova GroupTransition.
		 *
		 * @param source
		 *            O grupo de origem.
		 * @param consumed
		 *            O símbolo consumido.
		 * @param destination
		 *            O grupo de destino.
		 */
		private GroupTransition(Symbol consumed, Group destination) {
			this.consumed = consumed;
			this.destination = destination;
		}
	}

	/**
	 * Classe GroupBuilder. Serve para construir um novo {@link Group}, de forma
	 * que adições de estados e transições podem ser feitas somente através
	 * deste builder.
	 */
	@Data
	public final static class GroupBuilder {
		private final String name;
		private final HashSet<State> states;
		private final HashSet<GroupTransition> groupTransitions;

		private GroupBuilder() {
			this.name = "G" + (++NUM_GROUPS);
			this.states = new HashSet<>();
			this.groupTransitions = new HashSet<>();
		}

		public Group build() {
			return new Group(name, states, groupTransitions);
		}

		public GroupBuilder add(State state) {
			this.states.add(state);
			return this;
		}

		/**
		 * Adiciona uma transição de estado de um {@link Group} a outro, na
		 * forma de uma {@link GroupTransition}.
		 *
		 * @param consumed
		 *            O símbolo consumido.
		 * @param destination
		 *            O grupo de destino.
		 */
		public GroupBuilder addTransition(Symbol consumed, Group destination) {
			if (consumed == null || destination == null) {
				throw new ValidationException(ErrorType.MINIMIZE, "Destino ou símbolo nulo ou inexistente no alfabeto.");
			}
			for (GroupTransition transition : groupTransitions) {
				if (transition.getConsumed().equals(consumed)) {
					throw new ValidationException(ErrorType.MINIMIZE, "Já existe uma transição para o mesmo símbolo." + Constants.NEWLINE + "Grupo: " + this.getName() + ". Símbolo da transição: "
							+ consumed.getStr() + ".");
				}
			}
			groupTransitions.add(new GroupTransition(consumed, destination));
			return this;
		}
	}

	private static int NUM_GROUPS = 0;

	private final String name;

	@Getter(value = AccessLevel.NONE)
	private final LinkedHashSet<State> states;

	@Getter(value = AccessLevel.NONE)
	private final ImmutableSet<GroupTransition> groupTransitions;

	/**
	 * Instancia um novo grupo atribuindo um nome automaticamente, com base no
	 * número de grupos criados.
	 *
	 * @param states
	 *            Os estados do grupo
	 */
	private Group(String name, Collection<State> states, Collection<GroupTransition> groupTransitions) {
		this.states = new LinkedHashSet<>(states);
		this.name = name;
		this.groupTransitions = ImmutableSet.<GroupTransition> builder().addAll(groupTransitions).build();
	}

	/**
	 * Remove deste grupo os estados na coleção fornecida.
	 *
	 * @param states
	 *            Os estados
	 */
	public void remove(State... states) {
		for (State state : states) {
			this.states.remove(state);
		}
	}

	/**
	 * Remove deste grupo os estados na coleção fornecida.
	 *
	 * @param states
	 *            Os estados
	 */
	public void removeAll(Collection<State> states) {
		this.states.removeAll(states);
	}

	/**
	 * Verifica se um estado pertence ao grupo.
	 *
	 * @param state
	 *            O estado.
	 * @return true, se pertencer ao grupo.
	 */
	public boolean contains(State state) {
		return states.contains(state);
	}

	/**
	 * Retorna um novo {@link GroupBuilder}.
	 *
	 * @return O novo builder
	 */
	public static GroupBuilder builder() {
		return new GroupBuilder();
	}

	/**
	 * Reseta o número de grupos criados para 0. Deve ser usado SOMENTE quando
	 * se minimiza um novo {@link AFD} e já se usou a minimização antes.
	 */
	public static void reset() {
		NUM_GROUPS = 0;
	}

}
