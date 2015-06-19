package com.lfa.constants;

import java.util.Arrays;

import lombok.Getter;
import lombok.ToString;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.ImmutableSet;

/**
 * Classe de constantes utilizadas pelo programa e métodos auxiliares.
 *
 * @author Andre Brait (andrebrait@gmail.com)
 */
public class Constants {

	/**
	 * Ennum para identificar as letras que serão usadas na entrada para
	 * declarar estados, alfabeto, transições, estados iniciais e estados
	 * finais.
	 */
	@ToString(callSuper = false, of = "initialLetter")
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

	/** Os separadores na sintaxe do arquivo de entrada */
	public static final ImmutableSet<String> SEPARATORS = ImmutableSet.<String> builder().add(OPEN_PAR, CLOSE_PAR, COLON, COMMA, DASH, ARROW, OPEN_BRACKET, CLOSE_BRACKET, SEMICOLON).build();

	/** Os separadores na sintaxe do arquivo exceto ponto-e-vírgula */
	public static final ImmutableSet<String> SEPARATORS_MINUS_SEMICOLON = ImmutableSet.<String> builder().addAll(CollectionUtils.subtract(SEPARATORS, Arrays.asList(SEMICOLON))).build();

	/** Número de linhas ou separadores aceitas na entrada */
	public static final int LINES = 5;

	/* Constantes para facilitar formatação */
	public static final String TAB = "\t", D_TAB = "\t\t", NEWLINE = System.lineSeparator(), QUOTE = "\"";

	/** Constante da opção de arquivo de saída com AFD original */
	public static final String OPT_ORIGINAL = "-o";

	/** Constante da opção de arquivo de saída com AFD minimizado */
	public static final String OPT_MINIMIZED = "-m";

	/** O alfabeto do AFD. */
	public static Alphabet ALPHABET;

	/** O alfabeto do APD de sintaxe */
	public static Alphabet ALPHABET_APD;

	/** O alfabeto da pilha do APD. */
	public static Alphabet ALPHABET_APD_STACK;

	/** Constante da opção de arquivo de entrada */
	public static final String INPUT = "input";

	/** Constante da extensão dos arquivos de entrada. */
	public static final String DOT_EXT = ".dot", AFD_EXT = ".afd";

	/** Sinalizador de autoteste */
	public static final String AUTOTEST = "-autotest";

	/**
	 * O número máximo de parâmetros de linha de comando que este programa
	 * aceita
	 */
	public static final Integer PARAM_NUM = 4;

	/**
	 * Números possíveis de parâmetros (contando nome do programa e nomes dos
	 * arquivos) que este programa pode receber
	 */
	public static final ImmutableSet<Integer> POSSIBLE_PARAM_NUM = ImmutableSet.<Integer> builder().add(1, 2, 3, 4, 5, 6).build();
}
