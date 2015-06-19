package com.lfa.minimize;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.lfa.automata.afd.AFD;
import com.lfa.automata.afd.State;
import com.lfa.automata.afd.State.Transition;
import com.lfa.collections.ImmutableLinkedSet;
import com.lfa.minimize.Group.GroupBuilder;
import com.lfa.minimize.Group.GroupTransition;

/**
 * Classe AFDMinimizer. Contém métodos para minimizar um AFD e gerar um AFD de
 * saída.
 *
 * @author Andre Brait (andrebrait@gmail.com)
 */
public class AFDMinimizer {

	public static AFD minimize(AFD original) {

		HashSet<Group> groups = new HashSet<>();
		boolean modified = false;

		// Definindo a transição dos grupos iniciais:
		groups.addAll(GroupBuilder.buildInitialGroups(original));

		do {

			updateTransitions(groups);

			HashSet<GroupBuilder> groupsToAdd = new HashSet<>();
			HashMap<Group, HashSet<State>> statesToRemove = new HashMap<>();
			for (Group group : groups) {
				if (group.size() == 1) {
					continue;
				}
				HashSet<State> statesToRemoveFromThisGroup = new HashSet<>();
				HashSet<GroupBuilder> groupBuildersForThisGroup = new HashSet<>();
				for (State state : group.getStates()) {
					for (Transition trans : state.getTransitions()) {
						if (!group.containsTransition(trans.getConsumed(), findGroup(groups, trans.getDestination()))) {
							GroupBuilder groupBuilderToAdd = findGroupBuilder(groups, groupBuildersForThisGroup, state);
							if (groupBuilderToAdd != null) {
								groupBuilderToAdd.add(state);
							} else {
								groupBuildersForThisGroup.add(createGroup(groups, state));
							}
							statesToRemoveFromThisGroup.add(state);
							break;
						}
					}
				}
				groupsToAdd.addAll(groupBuildersForThisGroup);
				statesToRemove.put(group, statesToRemoveFromThisGroup);
			}
			modified = CollectionUtils.isNotEmpty(groupsToAdd);
			for (Entry<Group, HashSet<State>> entry : statesToRemove.entrySet()) {
				entry.getKey().removeAll(entry.getValue());
			}
			for (GroupBuilder newGroup : groupsToAdd) {
				groups.add(newGroup.build());
			}
		} while (modified);

		State minimizedInitialState = null;
		HashMap<Group, State> minimizedAFDStates = new HashMap<>();
		HashSet<State> minimizedFinalStates = new HashSet<>();
		for (Group group : groups) {
			State state = new State(group.getName());
			if (group.contains(original.getInitialState())) {
				minimizedInitialState = state;
			}
			if (group.containsAny(original.getFinalStates())) {
				minimizedFinalStates.add(state);
			}
			minimizedAFDStates.put(group, state);
		}

		for (Entry<Group, State> entry : minimizedAFDStates.entrySet()) {
			for (GroupTransition groupTransition : entry.getKey().getGroupTransitions()) {
				entry.getValue().addTransition(groupTransition.getConsumed(), minimizedAFDStates.get(groupTransition.getDestination()));
			}
		}

		return new AFD(minimizedInitialState, minimizedAFDStates.values(), minimizedFinalStates);

	}

	/**
	 * Atualiza as transições dos grupos com base nas últimas alterações
	 * ocorridas.
	 *
	 * @param groups
	 *            Os grupos.
	 */
	private static void updateTransitions(Set<Group> groups) {
		for (Group group : groups) {
			ImmutableLinkedSet.Builder<GroupTransition> groupTransitions = ImmutableLinkedSet.builder();
			for (Transition transition : group.getStates().iterator().next().getTransitions()) {
				groupTransitions.add(new GroupTransition(transition.getConsumed(), findGroup(groups, transition.getDestination())));
			}
			group.setGroupTransitions(groupTransitions.build());
		}
	}

	/**
	 * Cria um grupo com o estado passado.
	 *
	 * @param groups
	 *            O conjunto de grupos.
	 * @param state
	 *            O estado.
	 * @return O builder.
	 */
	private static GroupBuilder createGroup(Set<Group> groups, State state) {
		GroupBuilder builder = Group.builder().add(state);
		for (Transition trans : state.getTransitions()) {
			builder.addTransition(trans.getConsumed(), findGroup(groups, trans.getDestination()));
		}
		return builder;
	}

	/**
	 * Encontra o builder no qual um estado está.
	 *
	 * @param groups
	 *            O conjunto de grupos.
	 * @param groupBuilders
	 *            O conjunto de builders.
	 * @param state
	 *            O estado.
	 * @return O builder.
	 */
	private static GroupBuilder findGroupBuilder(Set<Group> groups, Set<GroupBuilder> groupBuilders, State state) {
		for (GroupBuilder builder : groupBuilders) {
			boolean found = true;
			for (Transition trans : state.getTransitions()) {
				if (!builder.containsTransition(trans.getConsumed(), findGroup(groups, trans.getDestination()))) {
					found = false;
					break;
				}
			}
			if (found) {
				return builder;
			}
		}
		return null;
	}

	/**
	 * Encontra o grupo no qual um estado está.
	 *
	 * @param groups
	 *            O conjunto de grupos.
	 * @param state
	 *            O estado.
	 * @return O grupo que possui o estado.
	 */
	private static Group findGroup(Set<Group> groups, State state) {
		for (Group group : groups) {
			if (group.contains(state)) {
				return group;
			}
		}
		return null;
	}
}
