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
import com.lfa.exception.ValidationException.ErrorType;

/**
 * Classe InputParser. É utilizada para processar o arquivo de entrada do
 * minimizador de AFDs.
 */
public class InputParser {

	/**
	 * Lê o arquivo de entrada do AFD e monta-o dde acordo.
	 *
	 * @param input
	 *            A string do arquivo de entrada. Pode conter espaços,
	 *            tabulações e novas linhas.
	 * @return O AFD.
	 */
	public static AFD parse(String input) {
		List<String> validatedInput = SyntaxMatcher.validate(input);
		List<List<String>> lineList = breakBySemicolon(validatedInput);
		if (lineList.size() != Constants.LINES) {
			throwGenericValidationException();
		}
		Map<String, State> states = parseStates(lineList);
		Constants.ALPHABET = parseAlphabet(lineList);
		parseAndAddTransitions(lineList, states);
		State initialState = parseInitialState(lineList, states);
		Set<State> finalStates = parseFinalStates(lineList, states);
		return new AFD(initialState, states.values(), finalStates);
	}

	/**
	 * Monta os estados do AFD, validando cada estado durante o processo.
	 *
	 * @param lineList
	 *            A lista de linhas do AFD, já sem separadores exceto
	 *            ponto-e-vírgula e sem espaços.
	 * @return O mapa de estados do AFD.
	 */
	private static Map<String, State> parseStates(List<List<String>> lineList) {
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

	/**
	 * Monta o alfabeto de acordo com o declarado.
	 *
	 * @param lineList
	 *            A lista de linhas do AFD, já sem separadores exceto
	 *            ponto-e-vírgula e sem espaços.
	 * @return O alfabeto.
	 */
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

	/**
	 * Obtem o estado inicial de acordo com o declarado.
	 *
	 * @param lineList
	 *            A lista de linhas do AFD, já sem separadores exceto
	 *            ponto-e-vírgula e sem espaços.
	 * @param stateMap
	 *            O mapa de estados do AFD.
	 * @return O estado inicial.
	 */
	private static State parseInitialState(List<List<String>> lineList, Map<String, State> stateMap) {
		Initial initial = Initial.INITIAL;
		List<String> line = lineList.get(initial.ordinal());
		checkInitial(line, initial);
		genericSizeCheck(line, initial);
		if (line.size() > 3) {
			throw new ValidationException(ErrorType.INPUT_PARSE, "Há mais de um estado inicial declarado.");
		}
		String name = line.get(2);
		checkDeclared(name, stateMap);
		return stateMap.get(name);
	}

	/**
	 * Obtem o estado inicial de acordo com o declarado.
	 *
	 * @param lineList
	 *            A lista de linhas do AFD, já sem separadores exceto
	 *            ponto-e-vírgula e sem espaços.
	 * @param stateMap
	 *            O mapa de estados do AFD.
	 * @return Set de estados finais.
	 */
	private static Set<State> parseFinalStates(List<List<String>> lineList, Map<String, State> stateMap) {
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

	/**
	 * Obtém as transições de estado e as adiciona nos estados correspondentes.
	 *
	 * @param lineList
	 *            A lista de linhas do AFD, já sem separadores exceto
	 *            ponto-e-vírgula e sem espaços.
	 * @param stateMap
	 *            O mapa de estados do AFD.
	 */
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

		List<String> subLine = line.subList(2, line.size());
		for (int i = 0; i < subLine.size(); i += transitionSize) {
			List<String> transition = subLine.subList(i, i + transitionSize);
			String sourceName = transition.get(0);
			checkDeclared(sourceName, stateMap);
			State source = stateMap.get(sourceName);
			List<State> destinations = new ArrayList<>();
			for (int j = 1; j < transition.size(); j++) {
				String destinationName = transition.get(j);
				checkDeclared(destinationName, stateMap);
				State destination = stateMap.get(destinationName);
				destinations.add(destination);
			}
			for (int k = 0; k < destinations.size(); k++) {
				source.addTransition(Constants.ALPHABET.getSymbol(k), destinations.get(k));
			}
		}

	}

	/**
	 * Separa a lista de strings de entrada por ponto-e-vírgula.
	 *
	 * @param inputList
	 *            A lista de Strings de entrada.
	 * @return A lista de lista de Strings obtidas da lista original
	 *         separando-se por ponto-vírgula.
	 */
	private static List<List<String>> breakBySemicolon(List<String> inputList) {
		List<List<String>> returnList = new ArrayList<>();
		for (int i = 0; i < inputList.size(); i++) {
			List<String> subList = new ArrayList<String>();
			for (int j = i; j < inputList.size(); j++) {
				String str = inputList.get(j);
				if (str.equals(Constants.SEMICOLON)) {
					i = j;
					break;
				}
				subList.add(str);
			}
			if (CollectionUtils.isNotEmpty(subList)) {
				returnList.add(subList);
			}
		}
		return returnList;
	}

	/**
	 * Verifica se a letra inicial da linha (ou separador) corresponde ao
	 * esperado para cada linha (ou separador).
	 *
	 * @param line
	 *            A linha
	 * @param initial
	 *            A inicial esperada para uma parte do AFD.
	 */
	private static void checkInitial(List<String> line, Initial initial) {
		if (!initial.getInitialLetter().equals(line.get(0))) {
			throwGenericValidationException();
		}
	}

	/**
	 * Retorna o número de parâmetros declarados para uma determinada linha ou
	 * separador.
	 *
	 * @param line
	 *            A linha
	 * @param initial
	 *            A inicial da parte do AFD
	 * @return O número de parâmetros.
	 */
	private static int getNumberOfParams(List<String> line, Initial initial) {
		try {
			Integer paramNumber = Integer.parseInt(line.get(1));
			return paramNumber.intValue();
		} catch (NumberFormatException ex) {
			throw new ValidationException(ErrorType.INPUT_PARSE, "Número de parâmetros deve ser um número válido." + Constants.NEWLINE + "Número da linha ou separador: " + (initial.ordinal() + 1)
					+ ".");
		}
	}

	/**
	 * Verificador de tamanho de parâmetros genérico (para todos os casos exceto
	 * parte do estado inicial e as transições) Compara o número de parâmetros
	 * declarados com o número declarado de parâmetros.
	 *
	 * @param line
	 *            A linha
	 * @param initial
	 *            A inicial da parte do AFD
	 */
	private static void genericSizeCheck(List<String> line, Initial initial) {
		int numParameters = getNumberOfParams(line, initial);
		if (numParameters + 2 != line.size()) {
			throwWrongParamNumberException(numParameters + 2 < line.size(), initial);
		}
	}

	/**
	 * Verifica se há valor repetido de uma chave num mapa
	 *
	 * @param <E>
	 *            O tipo do objeto da chave do mapa.
	 * @param key
	 *            A chave a ser verificada.
	 * @param map
	 *            O mapa
	 */
	private static <E> void checkRepeated(E key, Map<E, ?> map) {
		checkRepeated(key, map.keySet());
	}

	/**
	 * Verifica se há valor repetido em um conjunto
	 *
	 * @param <E>
	 *            O tipo do objeto o conjunto.
	 * @param value
	 *            O valor a ser verificado.
	 * @param set
	 *            O conjunto
	 */
	private static <E> void checkRepeated(E value, Set<E> set) {
		if (set.contains(value)) {
			throw new ValidationException(ErrorType.INPUT_PARSE, "Estados ou transições com declaração repetida.");
		}
	}

	/**
	 * Verifica se um estado já foi declarado.
	 *
	 * @param name
	 *            O nome do estado.
	 * @param map
	 *            O mapa de estados.
	 */
	private static void checkDeclared(String name, Map<String, State> map) {
		if (!map.containsKey(name)) {
			throw new ValidationException(ErrorType.INPUT_PARSE, "O estado " + name + " não foi declarado antes de ser usado em uma transição ou ser determinado como inicial ou final.");
		}
	}

	/**
	 * Joga uma exceção de validação de número de parâmetros errados.
	 *
	 * @param less
	 *            Indicador se a quantidade é menor ou maior que a esperada.
	 * @param initial
	 *            A inicial da linha na qual ocorreu a falha.
	 */
	private static void throwWrongParamNumberException(boolean less, Initial initial) {
		throw new ValidationException(ErrorType.INPUT_PARSE, "Há " + (less ? "menos" : "mais") + " parâmetros do que o declarado. Linha ou separador: " + (initial.ordinal() + 1) + ".");
	}

	/**
	 * Joga uma exceção de validação genérica.
	 */
	private static void throwGenericValidationException() {
		throw new ValidationException(ErrorType.INPUT_PARSE, "Arquivo de entrada em formato inválido: há parâmetros fora de ordem, faltantes ou em excesso.");
	}

}
