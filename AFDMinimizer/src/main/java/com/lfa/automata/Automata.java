package com.lfa.automata;

import com.lfa.automata.afd.State;
import com.lfa.collections.ImmutableLinkedSet;

/**
 * Interface Automata. Contém os métodos que todos os autômatos devem poder
 * executar, incluindo o processamento de uma entrada dada em uma String.
 *
 * @author Andre Brait (andrebrait@gmail.com)
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
	ImmutableLinkedSet<? extends State> getStates();

	/**
	 * Retorna o conjunto imutável de estados iniais de um autômato.
	 *
	 * @return Os estados finais do autômato.
	 */
	ImmutableLinkedSet<? extends State> getFinalStates();

	/**
	 * Retorna o tamanho do autômato.
	 *
	 * @return O tamanho do autômato.
	 */
	public int size();

}
