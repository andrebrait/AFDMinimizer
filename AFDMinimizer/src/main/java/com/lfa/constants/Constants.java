package com.lfa.constants;

import com.google.common.collect.ImmutableSet;

/**
 * Classe de constantes utilizadas pelo programa e métodos auxiliares.
 */
public class Constants {

	// Separadores aceitos no arquivo de entrada
	public static final ImmutableSet<String> SEPARATORS = ImmutableSet.<String> builder().add("(", ")", ",", ";", "->", "{", "}").build();

	/* Constantes para facilitar formatação */
	public static final String TAB = "\t", D_TAB = "\t\t", NEWLINE = "\n";

	public static final String OPT_ORIGINAL = "-o";
	public static final String OPT_MINIMIZED = "-m";

	/** O alfabeto do AFD. */
	public static Alphabet ALPHABET;

	/** O alfabeto do APD de sintaxe */
	public static final Alphabet ALPHABET_SYNTAX_APD = new Alphabet(SEPARATORS);
}