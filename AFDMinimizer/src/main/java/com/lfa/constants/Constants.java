package com.lfa.constants;

import java.util.Arrays;

import lombok.Getter;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.ImmutableSet;

/**
 * Classe de constantes utilizadas pelo programa e métodos auxiliares.
 */
public class Constants {

	/**
	 * Ennum para identificar as letras que serão usadas na entrada para
	 * declarar estados, alfabeto, transições, estados iniciais e estados
	 * finais.
	 */
	public static enum Initial {

		STATES("E"), ALPHABET("A"), TRANSITIONS("T"), INITIAL("I"), FINAL("F");

		@Getter
		private String initialLetter;

		private Initial(String initialLetter) {
			this.initialLetter = initialLetter;
		}
	}

	// Separadores aceitos no arquivo de entrada
	public static final String OPEN_PAR = "(", CLOSE_PAR = ")", COLON = ":", COMMA = ",", DASH = "-", ARROW = ">", OPEN_BRACKET = "{", CLOSE_BRACKET = "}", SEMICOLON = ";";
	public static final ImmutableSet<String> SEPARATORS = ImmutableSet.<String> builder().add(OPEN_PAR, CLOSE_PAR, COLON, COMMA, DASH, ARROW, OPEN_BRACKET, CLOSE_BRACKET).build();
	public static final ImmutableSet<String> SEPARATORS_MINUS_SEMICOLON = ImmutableSet.<String> builder().addAll(CollectionUtils.subtract(SEPARATORS, Arrays.asList(SEMICOLON))).build();
	public static final int LINES = 5;

	/* Constantes para facilitar formatação */
	public static final String TAB = "\t", D_TAB = "\t\t", NEWLINE = "\n";

	public static final String OPT_ORIGINAL = "-o";
	public static final String OPT_MINIMIZED = "-m";

	/** O alfabeto do AFD. */
	public static Alphabet ALPHABET;

	/** O alfabeto do APD de sintaxe */
	public static Alphabet ALPHABET_APD;

	/** O alfabeto da pilha do APD. */
	public static Alphabet ALPHABET_APD_STACK;
}
