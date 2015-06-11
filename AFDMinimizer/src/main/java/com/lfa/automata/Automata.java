package com.lfa.automata;

import com.google.common.collect.ImmutableSet;
import com.lfa.automata.afd.State;

/**
 * Interface Automata. Contém os métodos que todos os autômatos devem poder
 * executar, incluindo o processamento de uma entrada dada em uma String.
 */
public interface Automata {

	/**
	 * Executa o autômato utilizando uma string de entrada.
	 *
	 * @param str
	 *            A string de entrada.
	 */
	void run(String str);

	/**
	 * Retorna o estado inicial do autômato.
	 *
	 * @return Os estado inicial do autômato.
	 */
	State getInitialState();

	/**
	 * Retorna o conjunto imutável de estados de um autômato.
	 *
	 * @return Os estados do autômato.
	 */
	ImmutableSet<? extends State> getStates();

	/**
	 * Retorna o conjunto imutável de estados iniais de um autômato.
	 *
	 * @return Os estados finais do autômato.
	 */
	ImmutableSet<? extends State> getFinalStates();

}
