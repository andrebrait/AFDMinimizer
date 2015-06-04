package com.lfa.constants;

import java.util.Collection;
import java.util.HashSet;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.StringUtils;

/**
 * Classe Alphabet. Basicamente uma classe para armazenar os símbolos do
 * alfabeto do AFD.
 */
@Data
public final class Alphabet {

	public static final String LAMBDA = "\u03BB";

	/**
	 * Classe Symbol. Representa um símbolo do alfabeto.
	 */
	@Data
	@EqualsAndHashCode(of = "str")
	public final static class Symbol {

		private final String str;

		/**
		 * Instancia um novo Symbol.
		 *
		 * @param str
		 *            A string que representa símbolo gráfico.
		 */
		public Symbol(String str) {
			this.str = StringUtils.deleteWhitespace(str);
		}

		public String getStrOrLambda() {
			return StringUtils.isBlank(str) ? LAMBDA : str;
		}
	}

	private final HashSet<Symbol> symbolSet;

	/**
	 * Instantiates a new alphabet.
	 *
	 * @param symbols
	 *            the symbols
	 */
	public Alphabet(Collection<String> symbols) {
		symbolSet = new HashSet<>();
		for (String str : symbols) {
			symbolSet.add(new Symbol(str));
		}
	}

	/**
	 * Instantiates a new alphabet.
	 *
	 * @param symbol
	 *            the symbol
	 */
	public Alphabet(String... symbol) {
		symbolSet = new HashSet<>();
		for (String str : symbol) {
			symbolSet.add(new Symbol(str));
		}
	}

	/**
	 * Gets the symbol.
	 *
	 * @param str
	 *            the str
	 * @return the symbol
	 */
	public Symbol getSymbol(String str) {
		for (Symbol sym : symbolSet) {
			if (sym.equals(str)) {
				return sym;
			}
		}
		return null;
	}
}
