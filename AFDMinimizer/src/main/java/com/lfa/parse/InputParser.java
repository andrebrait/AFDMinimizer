package com.lfa.parse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.lfa.automata.afd.AFD;
import com.lfa.automata.afd.State;
import com.lfa.constants.Constants;
import com.lfa.exception.ValidationException;

/**
 * Classe InputParser. É utilizada para processar o arquivo de entrada do
 * minimizador de AFDs.
 */
public class InputParser {

	public static HashSet<State> states;

	public static AFD parse(String input) {
		List<String> inputList = SyntaxMatcher.validate(input);

	}

	private static HashSet<State> parseStates(List<String> inputList) {
		List<List<String>> lineList = breakBySemicolon(inputList);
		HashSet<State> returnSet = new HashSet<>();
		if (lineList.size() != Constants.LINES) {
			throwGenericValidationException();
		}
		for (int i = 0; i < lineList.size(); i++) {
			List<String> line = lineList.get(i);
			checkLine(i, line);
			for (int j = 0; j < line.size(); j++) {
				subList = line.subList(line., toIndex)
			}
		}
	}

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

	private static void checkLine(int i, List<String> line) {
		if (!Constants.LINE_MAP.get(i).equals(line.get(0))) {
			throwGenericValidationException();
		}
	}

	private static void throwGenericValidationException() {
		throw new ValidationException("Arquivo de entrada em formato inválido: há parâmetros faltantes ou em excesso.");
	}

}
