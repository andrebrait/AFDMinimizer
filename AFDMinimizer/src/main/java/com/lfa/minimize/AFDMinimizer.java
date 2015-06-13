package com.lfa.minimize;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.lfa.automata.afd.AFD;
import com.lfa.automata.afd.State;
import com.lfa.automata.afd.Transition;
import com.lfa.constants.Symbol;
import com.lfa.exception.ValidationException;
import com.lfa.exception.ValidationException.ErrorType;

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

		do {

		} while (modified);

	}

	private static void updateGroupTransitions(Set<Group> groups) {
		for (Group group : groups) {
			HashMap<Symbol, Group> groupTransitions = new HashMap<>();
			State state = group.getFirst();
			for (Transition transition : state.getTransitions()) {
				groupTransitions.put(transition.getConsumed(), findGroup(groups, transition.getDestination()));
			}
			group.setGroupTransitions(groupTransitions);
		}
	}

	private static Group findGroup(Set<Group> groups, State state) {
		for (Group group : groups) {
			if (group.contains(state)) {
				return group;
			}
		}
		throw new ValidationException(ErrorType.MINIMIZE, "Não existe grupo com o estado procurado: " + state);
	}
}
