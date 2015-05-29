package com.lfa.argsolve;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.lfa.constants.Constants;
import com.lfa.exception.ValidationException;

/**
 * Classe ArgumentResolver. Contém métodos e subclasses para o tratamento dos
 * argumentos do minimizador de AFDs
 */
public class ArgumentResolver {

	public static final String INPUT = "input";
	public static final String DOT_EXT = ".dot", AFD_EXT = ".afd";
	public static final Integer PARAM_NUM = 3;
	public static final List<Integer> POSSIBLE_PARAM_NUM = Arrays.asList(2, 4, 6);

	/**
	 * Resolve e valida os argumentos recebidos pelo programa. Retorna os
	 * arquivos utilizados para a E/S.
	 *
	 * @param Os
	 *            argumentos do programa.
	 * @return HashMap com as opções do programa e seus arquivos de E/S.
	 */
	public static HashMap<String, File> resolveArguments(String[] args) {
		List<String> paramOptions = Arrays.asList(Constants.OPT_ORIGINAL, Constants.OPT_MINIMIZED);
		HashMap<String, String> mapArgs = new HashMap<>(PARAM_NUM);
		if (!POSSIBLE_PARAM_NUM.contains(args.length)) {
			throwValidationException(args);
		}
		for (int i = 1; i < args.length; i++) {
			if (paramOptions.contains(args[i])) {
				if (!StringUtils.endsWithIgnoreCase(args[++i], DOT_EXT)) {
					throwValidationException(args);
				}
				mapArgs.put(args[i - 1], args[i]);
			} else if (!StringUtils.endsWithIgnoreCase(args[i], AFD_EXT)) {
				throwValidationException(args);
			} else {
				mapArgs.put(INPUT, args[i]);
			}
		}
		if (mapArgs.get(Constants.OPT_ORIGINAL).equals(mapArgs.get(Constants.OPT_MINIMIZED))) {
			throwValidationException(args);
		}
		HashMap<String, File> returnMap = new HashMap<>(PARAM_NUM);
		for (Entry<String, String> entry : mapArgs.entrySet()) {
			returnMap.put(entry.getKey(), new File(entry.getValue()));
		}
		return returnMap;
	}

	/*
	 * ## Montagem da mensagem e classe de exceção específicas do minimizador ##
	 */

	/**
	 * Joga uma ValidationException com base na lista de argumentos do programa
	 *
	 * @param argList
	 *            A lista de argumentos
	 */
	private static void throwValidationException(String[] args) {
		String message = "Usar: " + args[0] + " <Opções> [AFD de entrada em formato AFD]" + Constants.NEWLINE;
		message += Constants.TAB + "Opções:" + Constants.NEWLINE;
		message += Constants.D_TAB + Constants.OPT_ORIGINAL + Constants.TAB + "AFD original em formato DOT" + Constants.NEWLINE;
		message += Constants.D_TAB + Constants.OPT_MINIMIZED + Constants.TAB + "AFD reduzido em formato DOT";
		throw new ValidationException(message);
	}

}
