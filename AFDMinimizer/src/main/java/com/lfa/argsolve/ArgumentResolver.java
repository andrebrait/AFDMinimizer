package com.lfa.argsolve;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Classe ArgumentResolver. Contém métodos e subclasses para o tratamento dos
 * argumentos do minimizador de AFDs
 */
public class ArgumentResolver {

	public static final String ORIGINAL = "-o";
	public static final String MINIMIZED = "-m";
	public static final String INPUT = "input";
	public static final String DOT_EXT = ".dot", AFD_EXT = ".afd";
	public static final Integer PARAM_NUM = 3;

	/**
	 * Resolve e valida os argumentos recebidos pelo programa. Retorna os
	 * arquivos utilizados para a E/S.
	 *
	 * @param Os
	 *            argumentos do programa.
	 * @return HashMap com as opções do programa e seus arquivos de E/S.
	 */
	public static HashMap<String, File> resolveArguments(String[] args) {
		List<String> argList = Arrays.asList(args);
		List<String> paramOptions = Arrays.asList(ORIGINAL, MINIMIZED);
		HashMap<String, String> mapArgs = new HashMap<>(PARAM_NUM);
		if (argList.size() != 5 || !argList.containsAll(paramOptions)) {
			throwValidationException(argList);
		}
		for (int i = 1; i < 5; i++) {
			if (paramOptions.contains(args[i])) {
				if (!StringUtils.endsWithIgnoreCase(args[++i], DOT_EXT)) {
					throwValidationException(argList);
				}
				mapArgs.put(args[i - 1], args[i]);
			} else if (!StringUtils.endsWithIgnoreCase(args[i], AFD_EXT)) {
				throwValidationException(argList);
			} else {
				mapArgs.put(INPUT, args[i]);
			}
		}
		if (mapArgs.get(ORIGINAL).equals(mapArgs.get(MINIMIZED))) {
			throwValidationException(argList);
		}
		HashMap<String, File> returnMap = new HashMap<>(PARAM_NUM);
		returnMap.put(ORIGINAL, new File(mapArgs.get(ORIGINAL)));
		returnMap.put(MINIMIZED, new File(mapArgs.get(MINIMIZED)));
		returnMap.put(INPUT, new File(mapArgs.get(INPUT)));
		return returnMap;
	}

	/*
	 * ## Montagem da mensagem e classe de exceção específicas do minimizador ##
	 */

	/* Constantes para facilitar formatação */
	private static final String TAB = "\t", D_TAB = "\t\t", NEWLINE = "\n";

	/**
	 * Joga uma ValidationException com base na lista de argumentos do programa
	 *
	 * @param argList
	 *            A lista de argumentos
	 */
	private static void throwValidationException(List<String> argList) {
		String message = "Usar: " + argList.get(0) + " <Opções> [AFD de entrada em formato AFD]" + NEWLINE;
		message += TAB + "Opções:" + NEWLINE;
		message += D_TAB + ORIGINAL + TAB + "AFD original em formato DOT" + NEWLINE;
		message += D_TAB + MINIMIZED + TAB + "AFD reduzido em formato DOT";
		throw new ValidationException(message);
	}

	/**
	 * Classe ValidationException. É usada para lançar exceções de validação do
	 * minimizador.
	 */
	public static class ValidationException extends RuntimeException {

		private static final long serialVersionUID = 7895933445788956947L;

		/**
		 * Instancia uma nova ValidationException
		 *
		 * @param message
		 *            A mensagem
		 */
		public ValidationException(String message) {
			super(message);
		}

	}
}
