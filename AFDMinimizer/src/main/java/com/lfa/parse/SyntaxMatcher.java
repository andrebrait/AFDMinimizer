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
		APDState c = new APDState("C");
		APDState d = new APDState("D");
		APDState e = new APDState("E");

		a.addTransition("(", StringUtils.EMPTY, "ZYX", a);
		a.addTransition(")", "X", StringUtils.EMPTY, a);
		a.addTransition(":", "Y", StringUtils.EMPTY, a);
		a.addTransition(",", StringUtils.EMPTY, StringUtils.EMPTY, a);
		a.addTransition(";", "Z", "1", b);

		b.addTransition("(", StringUtils.EMPTY, "YX", b);
		b.addTransition(")", "X", StringUtils.EMPTY, b);
		b.addTransition(":", "Y", StringUtils.EMPTY, b);
		b.addTransition(",", StringUtils.EMPTY, StringUtils.EMPTY, b);
		b.addTransition(";", "1", "2", c);

		c.addTransition("(", StringUtils.EMPTY, "YX", c);
		c.addTransition(")", "X", StringUtils.EMPTY, c);
		c.addTransition(":", "Y", StringUtils.EMPTY, c);
		c.addTransition("-", "2", "K", c);
		c.addTransition(">", "K", "L", c);
		c.addTransition("{", "L", "M", c);
		c.addTransition(",", "M", "N", c);
		c.addTransition("}", "N", "O", c);
		c.addTransition(",", "O", "2", c);
		c.addTransition(";", "O", "3", d);

		d.addTransition("(", StringUtils.EMPTY, "YX", d);
		d.addTransition(")", "X", StringUtils.EMPTY, d);
		d.addTransition(":", "Y", StringUtils.EMPTY, d);
		d.addTransition(",", StringUtils.EMPTY, StringUtils.EMPTY, d);
		d.addTransition(";", "3", "4", e);

		e.addTransition("(", StringUtils.EMPTY, "YX", d);
		e.addTransition(")", "X", StringUtils.EMPTY, d);
		e.addTransition(":", "Y", StringUtils.EMPTY, d);
		e.addTransition(",", StringUtils.EMPTY, StringUtils.EMPTY, d);
		e.addTransition(";", "3", StringUtils.EMPTY, e);

		return new APD(a, Arrays.asList(a, b, c, d, e), Arrays.asList(e));
	}
}
