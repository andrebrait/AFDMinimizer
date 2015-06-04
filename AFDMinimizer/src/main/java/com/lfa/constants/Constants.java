package com.lfa.constants;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Classe de constantes utilizadas pelo programa e métodos auxiliares.
 */
public class Constants {

	// Separadores aceitos no arquivo de entrada
	public static final HashSet<String> SEPARATORS = new HashSet<>(Arrays.asList("(", ")", ",", ";", "->", "{", "}"));

	/* Constantes para facilitar formatação */
	public static final String TAB = "\t", D_TAB = "\t\t", NEWLINE = "\n";

	public static final String OPT_ORIGINAL = "-o";
	public static final String OPT_MINIMIZED = "-m";

	/** O alfabeto do AFD. */
	public static Alphabet ALPHABET;
}
