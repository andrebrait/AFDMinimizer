package com.lfa.argsolve;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableList;
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
	public static final ImmutableList<Integer> POSSIBLE_PARAM_NUM = ImmutableList.<Integer> builder().add(2, 4, 6).build();

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
		mapArgs.entrySet().forEach((entry) -> returnMap.put(entry.getKey(), new File(entry.getValue())));
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
		throw new ValidationException(new StringBuilder().append("Usar: ").append(args[0]).append(" <Opções> [AFD de entrada em formato AFD]").append(Constants.NEWLINE).append(Constants.TAB)
				.append("Opções:").append(Constants.NEWLINE).append(Constants.D_TAB).append(Constants.OPT_ORIGINAL).append(Constants.TAB).append("AFD original em formato DOT")
				.append(Constants.NEWLINE).append(Constants.D_TAB).append(Constants.OPT_MINIMIZED).append(Constants.TAB).append("AFD reduzido em formato DOT").toString());
	}
}
