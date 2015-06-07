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
		APDState in = new APDState("IN");
		APDState a = new APDState("A");
		APDState b = new APDState("B");
		APDState c = new APDState("C");
		in.addTransition(StringUtils.EMPTY, StringUtils.EMPTY, "Z", a);
		a.addTransition("(", StringUtils.EMPTY, "YX", a);
		a.addTransition(")", "X", StringUtils.EMPTY, a);
		a.addTransition(":", "Y", StringUtils.EMPTY, a);
		a.addTransition(",", StringUtils.EMPTY, StringUtils.EMPTY, a);
		a.addTransition(";", "Z", "1", b);

		b.addTransition("(", StringUtils.EMPTY, "YX", b);
		b.addTransition(")", "X", StringUtils.EMPTY, b);
		b.addTransition(":", "Y", StringUtils.EMPTY, b);
		b.addTransition(",", StringUtils.EMPTY, StringUtils.EMPTY, b);
		b.addTransition(";", "1", "2", c);

		APDState d = new APDState("D");


		APDState e = new APDState("E");

		// Segundo "loop" de estados, para A
		APDState f = new APDState("F");
		e.addTransition("(", StringUtils.EMPTY, "ZYX", f);
		APDState g = new APDState("G");
		f.addTransition(")", "X", StringUtils.EMPTY, g);
		APDState h = new APDState("H");
		g.addTransition(":", "Y", StringUtils.EMPTY, h);
		h.addTransition(",", StringUtils.EMPTY, StringUtils.EMPTY, h);
		APDState i = new APDState("I");
		h.addTransition(";", "Z", StringUtils.EMPTY, i);
		//"loop" de reconhecimento das trannsições com -> {,}, ... ;
		APDState j = new APDState("J");
		i.addTransition("")

		return new APD(a, Arrays.asList(a, b, c, d, e), Arrays.asList(e));
	}
}
