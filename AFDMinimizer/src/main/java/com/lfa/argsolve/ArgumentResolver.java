package com.lfa.argsolve;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;
import com.lfa.constants.Constants;
import com.lfa.exception.ValidationException;
import com.lfa.exception.ValidationException.ErrorType;

/**
 * Classe ArgumentResolver. Contém métodos e subclasses para o tratamento dos
 * argumentos do minimizador de AFDs
 */
public class ArgumentResolver {

	/**
	 * Resolve e valida os argumentos recebidos pelo programa. Retorna os
	 * arquivos utilizados para a E/S.
	 *
	 * @param Os
	 *            argumentos do programa.
	 * @return HashMap com as opções do programa e seus arquivos de E/S.
	 */
	public static ImmutableMap<String, File> resolveArguments(String[] args) {
		List<String> paramOptions = Arrays.asList(Constants.OPT_ORIGINAL, Constants.OPT_MINIMIZED);
		HashMap<String, String> mapArgs = new HashMap<>(Constants.PARAM_NUM);
		if (!Constants.POSSIBLE_PARAM_NUM.contains(args.length)) {
			throwValidationException();
		}
		try {
			for (int i = 0; i < args.length; i++) {
				if (paramOptions.contains(args[i])) {
					if (!StringUtils.endsWithIgnoreCase(args[++i], Constants.DOT_EXT)) {
						throwValidationException();
					}
					mapArgs.put(args[i - 1], args[i]);
				} else if (StringUtils.equals(args[i].trim(), Constants.AUTOTEST)) {
					mapArgs.put(Constants.AUTOTEST, args[i]);
				} else if (!StringUtils.endsWithIgnoreCase(args[i], Constants.AFD_EXT)) {
					throwValidationException();
				} else {
					mapArgs.put(Constants.INPUT, args[i]);
				}
			}
			if (mapArgs.containsKey(Constants.OPT_ORIGINAL) && mapArgs.containsKey(Constants.OPT_MINIMIZED)) {
				if (mapArgs.get(Constants.OPT_ORIGINAL).equals(mapArgs.get(Constants.OPT_MINIMIZED))) {
					throwValidationException();
				}
			}
		} catch (Exception ex) {
			if (ex instanceof ValidationException) {
				throw ex;
			}
			throwValidationException();
		}
		ImmutableMap.Builder<String, File> returnMap = ImmutableMap.builder();
		mapArgs.entrySet().forEach(entry -> returnMap.put(entry.getKey(), new File(entry.getValue())));
		return returnMap.build();
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
	private static void throwValidationException() {
		throw new ValidationException(ErrorType.OTHER, new StringBuilder().append("Usar: java -jar [nome do jar]").append(" <Opções> [AFD de entrada em formato AFD]").append(Constants.NEWLINE)
				.append(Constants.TAB).append("Opções:").append(Constants.NEWLINE).append(Constants.D_TAB).append(Constants.OPT_ORIGINAL).append(Constants.TAB).append("AFD original em formato DOT")
				.append(Constants.NEWLINE).append(Constants.D_TAB).append(Constants.OPT_MINIMIZED).append(Constants.TAB).append("AFD reduzido em formato DOT").append(Constants.NEWLINE)
				.append(Constants.D_TAB).append(Constants.AUTOTEST).append(Constants.TAB).append("Habilita autoteste do AFD com palavras geradas a partir do AFD original").toString());
	}
}
