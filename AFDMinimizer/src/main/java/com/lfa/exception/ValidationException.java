package com.lfa.exception;

/**
 * Classe ValidationException. É usada para lançar exceções de validação do
 * minimizador.
 */
public class ValidationException extends RuntimeException {

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