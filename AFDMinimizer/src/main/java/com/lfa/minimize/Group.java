package com.lfa.minimize;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.apache.commons.collections4.CollectionUtils;

import com.lfa.automata.afd.AFD;
import com.lfa.automata.afd.State;
import com.lfa.automata.afd.State.Transition;
import com.lfa.collections.ImmutableLinkedSet;
import com.lfa.constants.Alphabet.Symbol;
import com.lfa.constants.Constants;
import com.lfa.exception.ValidationException;
import com.lfa.exception.ValidationException.ErrorType;

/**
 * Classe Group. É a representação de um grupo de minimização. Assim como o
 * grupo no algoritmo, não é possível adicionar estados a um grupo, apenas
 * remover. Apesar de muitas similaridades com {@link State}, a extensão não é
 * viável dada a diferença semântica entre ambas.
 *
 * @author Andre Brait (andrebrait@gmail.com)
 */
@Data
@EqualsAndHashCode(callSuper = false, of = "name")
@ToString(callSuper = false, of = "name")
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

		@Setter(AccessLevel.PROTECTED)
		private Group destination;

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
		protected GroupTransition(Symbol consumed, Group destination) {
			this.consumed = consumed;
			this.destination = destination;
		}
	}

	/**
	 * Classe GroupBuilder. Serve para construir um novo {@link Group}, de forma
	 * que adições de estados e transições podem ser feitas somente através
	 * deste builder.
	 */
	@ToString(callSuper = false, of = "name")
	public final static class GroupBuilder {

		private static int NUM_GROUPS = 0;

		private final String name;
		private final LinkedHashSet<State> states;
		private final LinkedHashSet<GroupTransition> groupTransitions;
		private boolean built;

		private static boolean builtInitial = false;

		/**
		 * Reseta o número de grupos criados para 0. Deve ser usado SOMENTE
		 * quando se minimiza um novo {@link AFD} e já se usou a minimização
		 * antes.
		 */
		public static void reset() {
			builtInitial = false;
			NUM_GROUPS = 0;
		}

		/**
		 * Instancia um novo GroupBuilder
		 */
		private GroupBuilder() {
			this.name = "G" + (++NUM_GROUPS);
			this.states = new LinkedHashSet<>();
			this.groupTransitions = new LinkedHashSet<>();
			this.built = false;
		}

		/**
		 * Termina a construção do grupo e retorna o grupo construído.
		 *
		 * @return O grupo construído.
		 */
		public Group build() {
			if (built) {
				throw new ValidationException(ErrorType.MINIMIZE, "Tentativa de reuso de um builder de grupo.");
			}
			if (!builtInitial) {
				throw new ValidationException(ErrorType.MINIMIZE, "Grupos iniciais não foram construídos ainda.");
			}
			built = true;
			return buildProcess();
		}

		/**
		 * Processo de construção de um grupo, para uso interno da classe.
		 *
		 * @return O novo grupo construído.
		 */
		private Group buildProcess() {
			return new Group(name, states, groupTransitions);
		}

		/**
		 * Constrói os grupos iniciais de um AFD.
		 *
		 * @param original
		 *            O AFD original.
		 * @return the O conjunto de estados iniciais criados.
		 */
		public static Set<Group> buildInitialGroups(AFD original) {

			if (builtInitial) {
				throw new ValidationException(ErrorType.MINIMIZE, "Grupos iniciais já criados.");
			}

			try {
				Group nonFinalGroup = builder().addAll(CollectionUtils.removeAll(original.getStates(), original.getFinalStates())).buildProcess();
				Group finalGroup = builder().addAll(original.getFinalStates()).buildProcess();

				builtInitial = true;

				return new HashSet<>(Arrays.asList(nonFinalGroup, finalGroup));

			} catch (Throwable ex) {
				reset();
				throw ex;
			}
		}

		/**
		 * Adiciona um estado aos estados do grupo a ser construído.
		 *
		 * @param state
		 *            O estado.
		 * @return Este builder.
		 */
		public GroupBuilder add(State state) {
			this.states.add(state);
			return this;
		}

		/**
		 * Adiciona todos os estados recebidos aos estados do grupo a ser
		 * construído.
		 *
		 * @param state
		 *            Os estados.
		 * @return Este builder.
		 */
		public GroupBuilder addAll(Collection<State> state) {
			this.states.addAll(state);
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
		 * @return Este builder.
		 */
		public GroupBuilder addTransition(Symbol consumed, Group destination) {
			if (consumed == null || destination == null) {
				throw new ValidationException(ErrorType.MINIMIZE, "Destino ou símbolo nulo ou inexistente no alfabeto.");
			}
			for (GroupTransition transition : groupTransitions) {
				if (transition.getConsumed().equals(consumed)) {
					throw new ValidationException(ErrorType.MINIMIZE, "Já existe uma transição para o mesmo símbolo." + Constants.NEWLINE + "Grupo: " + name + ". Símbolo da transição: "
							+ consumed.getStr() + ".");
				}
			}
			groupTransitions.add(new GroupTransition(consumed, destination));
			return this;
		}

		/**
		 * Adiciona todas as transições do mapa, utilizando o símbolo chave e o
		 * grupo associado como destino.
		 *
		 * @param destinationMap
		 *            Mapa de Símbolos e Grupos de destino associados.
		 * @return Este builder.
		 */
		public GroupBuilder addAllTransitions(Map<Symbol, Group> destinationMap) {
			for (Entry<Symbol, Group> entry : destinationMap.entrySet()) {
				addTransition(entry.getKey(), entry.getValue());
			}
			return this;
		}

		/**
		 * Verifica se o grupo a ser costruído por este builder conterá o estado
		 * passado.
		 *
		 * @param state
		 *            O estado.
		 * @return true, se contiver.
		 */
		public boolean contains(State state) {
			return states.contains(state);
		}

		/**
		 * Verifica se o grupo a ser costruído por este builder conterá a
		 * transição passada.
		 *
		 * @param consumed
		 *            O símbolo consumido pela transição.
		 * @param destination
		 *            O grupo de destino.
		 * @return true, se contiver.
		 */
		public boolean containsTransition(Symbol consumed, Group destination) {
			return groupTransitions.contains(new GroupTransition(consumed, destination));
		}

	}

	private final String name;

	@Getter(value = AccessLevel.PROTECTED)
	private final LinkedHashSet<State> states;

	@Getter(value = AccessLevel.PROTECTED)
	@Setter(value = AccessLevel.PROTECTED)
	private ImmutableLinkedSet<GroupTransition> groupTransitions;

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
		this.groupTransitions = ImmutableLinkedSet.<GroupTransition> builder().addAll(groupTransitions).build();
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
	 * Retorna o tamanho do grupo.
	 *
	 * @return O tamanho do grupo.
	 */
	public int size() {
		return this.states.size();
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
	 * Verifica se qualquer um dos estados pertence ao grupo.
	 *
	 * @param states
	 *            Os estados
	 * @return true, se ao menos um pertencer ao grupo
	 */
	public boolean containsAny(Collection<State> states) {
		return CollectionUtils.containsAny(getStates(), states);
	}

	/**
	 * Verifica se o grupo contém a transição passada.
	 *
	 * @param consumed
	 *            O símbolo consumido pela transição.
	 * @param destination
	 *            O grupo de destino.
	 * @return true, se contiver.
	 */
	public boolean containsTransition(Symbol consumed, Group destination) {
		return groupTransitions.contains(new GroupTransition(consumed, destination));
	}

	/**
	 * Retorna um novo {@link GroupBuilder}.
	 *
	 * @return O novo builder
	 */
	public static GroupBuilder builder() {
		return new GroupBuilder();
	}

}
