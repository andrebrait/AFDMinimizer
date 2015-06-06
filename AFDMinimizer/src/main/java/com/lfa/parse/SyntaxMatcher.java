package com.lfa.parse;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lfa.automata.apd.APD;
import com.lfa.constants.Constants;

/**
 * Classe SyntaxMatcher. Contém métodos para validar a sintaxe do arquivo de
 * entrada de dados.
 */
public class SyntaxMatcher {

	/**
	 * Validate.
	 *
	 * Recebe o arquivo de input em uma linha só e valida a mesma.
	 *
	 * @param input
	 *            A entrada de dados linearizada e sem espaços.
	 */
	public static String validate(String input) {
		input = StringUtils.deleteWhitespace(input);
		List<String> decomposedInput = Arrays.asList(StringUtils.splitByCharacterType(input.toUpperCase()));
		decomposedInput.retainAll(Constants.SEPARATORS);

		return input;
	}

	private static APD createValidationAPD() {
		return null;
	}

}
