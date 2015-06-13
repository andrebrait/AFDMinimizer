package com.lfa.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * Classe ValidationException. É usada para lançar exceções de validação do
 * minimizador.
 */
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 7895933445788956947L;

	/**
	 * Enum ErrorType. Contém apenas algumas indicações de prefixos para a
	 * mensagem de erro.
	 */
	@ToString(callSuper = false, of = "prefix")
	public static enum ErrorType {

		AFD("#AFD#: "), APD("#APD#: "), INPUT_PARSE("#InputParse#: "), OTHER(""), MINIMIZE("#Minimize# :");

		@Getter
		private String prefix;

		private ErrorType(String prefix) {
			this.prefix = prefix;
		}
	}

	/**
	 * Instancia uma nova ValidationException.
	 *
	 * @param error
	 *            O tipo do erro
	 * @param message
	 *            A mensagem
	 */
	public ValidationException(ErrorType error, String message) {
		super(error.getPrefix() + message);
	}

}