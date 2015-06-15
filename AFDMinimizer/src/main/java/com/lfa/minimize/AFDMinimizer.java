package com.lfa.minimize;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.ImmutableSet;
import com.lfa.automata.afd.AFD;
import com.lfa.automata.afd.State;
import com.lfa.automata.afd.State.Transition;
import com.lfa.minimize.Group.GroupBuilder;
import com.lfa.minimize.Group.GroupTransition;

/**
 * Classe AFDMinimizer. Contém métodos para minimizar um AFD e gerar um AFD de
 * saída.
 */
public class AFDMinimizer {

	public static AFD minimize(AFD original) {

		HashSet<Group> groups = new HashSet<>();
		boolean modified = false;

		// Definindo a transição dos grupos iniciais:
		groups.addAll(GroupBuilder.buildInitialGroups(original));

		do {
			HashSet<GroupBuilder> groupsToAdd = new HashSet<>();
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
				group.removeAll(statesToRemoveFromThisGroup);
			}
			modified = CollectionUtils.isNotEmpty(groupsToAdd);
			for (GroupBuilder newGroup : groupsToAdd) {
				groups.add(newGroup.build());
			}
		} while (modified);

		updateTransitions(groups);

		State minimizedInitialState = null;
		HashMap<Group, State> minimizedAFDStates = new HashMap<>();
		HashSet<State> minimizedFinalStates = new HashSet<>();
		for (Group group : groups) {
			State state = new State(group.getName());
			if (group.contains(original.getInitialState())) {
				minimizedInitialState = state;
			} else if (group.containsAny(original.getFinalStates())) {
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

	private static void updateTransitions(Set<Group> groups) {
		for (Group group : groups) {
			ImmutableSet.Builder<GroupTransition> groupTransitions = ImmutableSet.builder();
			for (Transition transition : group.getStates().iterator().next().getTransitions()) {
				groupTransitions.add(new GroupTransition(transition.getConsumed(), findGroup(groups, transition.getDestination())));
			}
			group.setGroupTransitions(groupTransitions.build());
		}
	}

	private static GroupBuilder createGroup(Set<Group> groups, State state) {
		GroupBuilder builder = Group.builder().add(state);
		for (Transition trans : state.getTransitions()) {
			builder.addTransition(trans.getConsumed(), findGroup(groups, trans.getDestination()));
		}
		return builder;
	}

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

	private static Group findGroup(Set<Group> groups, State state) {
		for (Group group : groups) {
			if (group.contains(state)) {
				return group;
			}
		}
		return null;
	}
}
