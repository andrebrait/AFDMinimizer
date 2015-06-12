package com.lfa.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.lfa.automata.afd.AFD;
import com.lfa.automata.afd.State;
import com.lfa.constants.Alphabet;
import com.lfa.constants.Constants;
import com.lfa.constants.Constants.Initial;
import com.lfa.exception.ValidationException;

/**
 * Classe InputParser. É utilizada para processar o arquivo de entrada do
 * minimizador de AFDs.
 */
public class InputParser {

	public static AFD parse(String input) {
		List<String> validatedInput = SyntaxMatcher.validate(input);
		List<List<String>> lineList = breakBySemicolon(validatedInput);
		if (lineList.size() != Constants.LINES) {
			throwGenericValidationException();
		}
		HashMap<String, State> states = parseStates(lineList);
		Constants.ALPHABET = parseAlphabet(lineList);
		// TODO Parse de transições
		State initialState = parseInitialState(lineList, states);
		HashSet<State> finalStates = parseFinalStates(lineList, states);
	}

	private static HashMap<String, State> parseStates(List<List<String>> lineList) {
		Initial initial = Initial.STATES;
		List<String> line = lineList.get(initial.ordinal());
		checkInitial(line, initial);
		genericSizeCheck(line, initial);
		HashMap<String, State> returnMap = new HashMap<>(line.size() - 2);
		for (int i = 2; i < line.size(); i++) {
			State state = new State(line.get(i));
			checkRepeated(state.getName(), returnMap);
			returnMap.put(state.getName(), state);
		}
		return returnMap;
	}

	private static Alphabet parseAlphabet(List<List<String>> lineList) {
		Initial initial = Initial.ALPHABET;
		List<String> line = lineList.get(initial.ordinal());
		checkInitial(line, initial);
		genericSizeCheck(line, initial);
		LinkedHashSet<String> symbolSet = new LinkedHashSet<>(line.size() - 2);
		for (int i = 2; i < line.size(); i++) {
			String str = line.get(i);
			checkRepeated(str, symbolSet);
			symbolSet.add(str);
		}
		return new Alphabet(symbolSet);
	}

	private static State parseInitialState(List<List<String>> lineList, Map<String, State> stateMap) {
		Initial initial = Initial.INITIAL;
		List<String> line = lineList.get(initial.ordinal());
		checkInitial(line, initial);
		genericSizeCheck(line, initial);
		if (line.size() > 3) {
			throw new ValidationException("Há mais de um estado inicial declarado.");
		}
		String name = line.get(2);
		checkDeclared(name, stateMap);
		return stateMap.get(name);
	}

	private static HashSet<State> parseFinalStates(List<List<String>> lineList, Map<String, State> stateMap) {
		Initial initial = Initial.FINAL;
		List<String> line = lineList.get(initial.ordinal());
		checkInitial(line, initial);
		genericSizeCheck(line, initial);
		HashSet<State> states = new HashSet<>(line.size() - 2);
		for (int i = 2; i < line.size(); i++) {
			String str = line.get(i);
			checkDeclared(str, stateMap);
			State state = stateMap.get(str);
			checkRepeated(state, states);
			states.add(state);
		}
		return states;
	}

	private static void parseAndAddTransitions(List<List<String>> lineList, Map<String, State> stateMap) {
		Initial initial = Initial.TRANSITIONS;
		List<String> line = lineList.get(initial.ordinal());
		checkInitial(line, initial);

		// Checagem de tamanho especial para as transições
		int numDeclaredTransitions = getNumberOfParams(line, initial);

		int realLineSize = line.size() - 2;

		// Sabendo que as transições de cada estado são expressas como
		// (n+1)tuplas de valores em que n é o número de símbolos no alfabeto e
		// o símbolo a mais é o estado de origem, podemos calcular o número de
		// entradas neste campo dividindo-o em listas de n+1 elementos.
		int transitionSize = Constants.ALPHABET.size() + 1;
		int modTransitionSize = realLineSize % transitionSize;

		if (modTransitionSize != 0) {
			throwGenericValidationException();
		}

		int numActualTransitions = realLineSize / transitionSize;
		if (numActualTransitions != numDeclaredTransitions) {
			throwWrongParamNumberException(numActualTransitions < numDeclaredTransitions, initial);
		}

		List<List<String>> transitionStrings = new ArrayList<>(numDeclaredTransitions);
		List<String> subLine = line.subList(2, line.size());
		for (int i = 0; i < line.size(); i += transitionSize) {
			transitionStrings.add(subLine.subList(i, i + transitionSize));
		}
	}

	private static List<List<String>> breakBySemicolon(List<String> inputList) {
		List<List<String>> returnList = new ArrayList<>();
		int endIndex = 0;
		for (int i = 0; i < inputList.size(); i += endIndex) {
			endIndex = inputList.indexOf(Constants.SEMICOLON) + 1;
			List<String> toAdd = inputList.subList(i, endIndex);
			if(CollectionUtils.isNotEmpty(toAdd))
			returnList.add(toAdd);
		}
		return returnList;
	}

	private static void checkInitial(List<String> line, Initial initial) {
		if (!initial.getInitialLetter().equals(line.get(0))) {
			throwGenericValidationException();
		}
	}

	private static int getNumberOfParams(List<String> line, Initial initial) {
		try {
			Integer paramNumber = Integer.parseInt(line.get(1));
			return paramNumber.intValue();
		} catch (NumberFormatException ex) {
			throw new ValidationException("Número de parâmetros deve ser um número válido." + Constants.NEWLINE + "Número da linha ou separador: " + (initial.ordinal() + 1) + ".");
		}
	}

	private static void genericSizeCheck(List<String> line, Initial initial) {
		int numParameters = getNumberOfParams(line, initial);
		if (numParameters + 2 != line.size()) {
			throwWrongParamNumberException(numParameters + 2 < line.size(), initial);
		}
	}

	private static <E> void checkRepeated(E key, Map<E, ?> map) {
		checkRepeated(key, map.keySet());
	}

	private static <E> void checkRepeated(E value, Set<E> set) {
		if (set.contains(value)) {
			throw new ValidationException("Estados ou transições com declaração repetida.");
		}
	}

	private static void checkDeclared(String name, Map<String, State> map) {
		if (!map.containsKey(name)) {
			throw new ValidationException("O estado " + name + " não foi declarado antes de ser usado em uma transição ou ser determinado como inicial ou final.");
		}
	}

	private static void throwWrongParamNumberException(boolean less, Initial initial) {
		throw new ValidationException("Há " + (less ? "menos" : "mais") + " parâmetros do que o declarado. Linha ou separador: " + (initial.ordinal() + 1) + ".");
	}

	private static void throwGenericValidationException() {
		throw new ValidationException("Arquivo de entrada em formato inválido: há parâmetros fora de ordem, faltantes ou em excesso.");
	}

}
