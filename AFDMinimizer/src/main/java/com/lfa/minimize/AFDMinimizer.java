package com.lfa.minimize;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.lfa.automata.afd.AFD;
import com.lfa.automata.afd.State;
import com.lfa.automata.afd.State.Transition;
import com.lfa.constants.Alphabet.Symbol;

/**
 * Classe AFDMinimizer. Contém métodos para minimizar um AFD e gerar um AFD de
 * saída.
 */
public class AFDMinimizer {

	public static AFD minimize(AFD original) {

		int groupNum = 1;
		HashSet<Group> groups = new HashSet<>();
		boolean modified;

		// Grupo de estados não-finais
		groups.add(new Group(CollectionUtils.removeAll(original.getStates(), original.getFinalStates())));

		// Grupo de estados finais
		groups.add(new Group(original.getFinalStates()));

		// Atualizando as transições características de cada grupo com base no
		// primeiro estado de cada um.
		updateGroupTransitions(groups);

		do {
			HashSet<Group> newGroups = new HashSet<>();
			for (Group group : groups) {
				Iterator<State> i = group.iterator();
				while (i.hasNext()) {
					State current = i.next();
					for (Transition transition : current.getTransitions()) {
						if (!matchTransitions(current, group)) {

						}
					}
				}
			}
		} while (modified);

	}

	private static void updateGroupTransitions(Set<Group> groups) {
		for (Group group : groups) {
			updateGroupTransition(group, groups);
		}
	}

	private static void updateGroupTransition(Group group, Set<Group> groups) {
		LinkedHashMap<Symbol, Group> groupTransitions = new LinkedHashMap<>();
		State state = group.iterator().next();
		for (Transition transition : state.getTransitions()) {
			groupTransitions.put(transition.getConsumed(), findGroup(groups, transition.getDestination()));
		}
		group.setGroupTransitions(groupTransitions);
	}

	private static Group findGroup(Set<Group> groups, State state) {
		for (Group group : groups) {
			if (group.contains(state)) {
				return group;
			}
		}
		return null;
	}

	private static Group findDestinationGroup(State state, Set<Group> groups) {
		Iterator<Transition> trans = state.getTransitions().iterator();
		while (trans.hasNext()) {
			Transition currentTrans = trans.next();
			State destination = currentTrans.getDestination();
			Group destinationGroup = group.getGroupTransitions().get(currentTrans.getConsumed());
			match = match && group.contains(state) && destinationGroup.contains(destination);
		}
	}

	private static boolean matchTransitions(State state, Group group) {
		boolean match = true;
		Iterator<Transition> trans = state.getTransitions().iterator();
		while (trans.hasNext() && match) {
			Transition currentTrans = trans.next();
			State destination = currentTrans.getDestination();
			Group destinationGroup = group.getGroupTransitions().get(currentTrans.getConsumed());
			match = match && group.contains(state) && destinationGroup.contains(destination);
		}
		return match;
	}
}
