package com.lfa.parse;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.lfa.automata.apd.APD;
import com.lfa.automata.apd.APDState;
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
		Collection<String> intersect = CollectionUtils.retainAll(decomposedInput, Constants.SEPARATORS);
		createValidationAPD().run(StringUtils.join(intersect.toArray(), StringUtils.EMPTY));
		return input;
	}

	private static APD createValidationAPD() {
		APDState a = new APDState("A");
		APDState b = new APDState("B");
		a.addTransition("(", StringUtils.EMPTY, "FZYX", b);
		APDState c = new APDState("C");
		b.addTransition(")", "X", StringUtils.EMPTY, c);
		APDState d = new APDState("D");
		c.addTransition(":", "Y", StringUtils.EMPTY, d);
		APDState e = new APDState("E");
		d.addTransition(",", StringUtils.EMPTY, StringUtils.EMPTY, d);
		d.addTransition(";", "Z", StringUtils.EMPTY, e);

		return new APD(a, Arrays.asList(a, b, c, d, e), Arrays.asList(e));
	}

}
