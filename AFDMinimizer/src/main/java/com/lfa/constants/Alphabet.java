package com.lfa.constants;

import java.util.Arrays;
import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

/**
 * Classe Alphabet. Basicamente uma classe para armazenar os símbolos do
 * alfabeto do AFD.
 */
@Data
public final class Alphabet {

	public static final String LAMBDA = "\u03BB";

	private final ImmutableMap<String, Symbol> symbolMap;

	/**
	 * Classe Symbol. Representa um símbolo do alfabeto.
	 */
	@Data
	@EqualsAndHashCode(of = "str")
	public final static class Symbol {

		private final String str;

		/**
		 * Instancia um novo Symbol. Construtor protegido para uso somente por
		 * classes do pacote.
		 *
		 * @param str
		 *            A string que representa símbolo gráfico.
		 */
		protected Symbol(String str) {
			this.str = str;
		}

	}

	/**
	 * Instancia um novo alfabeto com o conjunto de símbolos passado neste
	 * construtor.
	 *
	 * @param symbols
	 *            Os símbolos do alfabeto.
	 */
	public Alphabet(String... symbol) {
		this(Arrays.asList(symbol));
	}

	/**
	 * Instancia um novo alfabeto com o conjunto de símbolos passado neste
	 * construtor.
	 *
	 * @param symbol
	 *            Os símbolos do alfabeto.
	 */
	public Alphabet(Collection<String> symbols) {
		ImmutableMap.Builder<String, Symbol> builder = new ImmutableMap.Builder<>();
		for (String str : symbols) {
			str = treat(str);
			builder.put(str, new Symbol(str));
		}
		symbolMap = builder.build();
	}

	/**
	 * Retorna o símbolo correspondendo à String passada, ou nulo se não houver.
	 *
	 * @param str
	 *            A string do símbolo.
	 * @return O símbolo correspondente.
	 */
	public Symbol getSymbol(String str) {
		str = treat(str);
		return symbolMap.get(str);
	}

	/**
	 * Trata a String do símbolo de forma que fique apropriada ao uso no
	 * AFD/APD.
	 *
	 * @param str
	 *            A string do símbolo.
	 * @return A string tratada.
	 */
	private String treat(String str) {
		return StringUtils.isBlank(str) ? LAMBDA : StringUtils.deleteWhitespace(str);
	}
}
