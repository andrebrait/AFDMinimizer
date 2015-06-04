package com.lfa.automata.apd;

import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.collections4.CollectionUtils;

import com.lfa.automata.afd.State;
import com.lfa.constants.Alphabet;
import com.lfa.constants.Alphabet.Symbol;
import com.lfa.constants.Constants;
import com.lfa.exception.ValidationException;

/**
 * Classe APDState. Classe desenhada para representar os estados de um APD. Não
 * suporta os métodos de adição da classe de estados do AFD, apesar de ter uma
 * colocação equivalente na modelagem dos autômatos.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class APDState extends State {

	/**
	 * Instancia um novo estado de APD.
	 *
	 * @param name
	 *            O nome do estado.
	 */
	public APDState(String name) {
		super(name);
	}

	@Deprecated
	@Override
	public void addTransition(String consumed, State destination) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operação não suportada para APD.");
	}

	@Deprecated
	@Override
	public void addTransition(Symbol consumed, State destination) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operação não suportada para APD.");
	}

	/**
	 * Adiciona uma transição de APD.
	 *
	 * @param consumed
	 *            A string do símbolo consumido.
	 * @param toPop
	 *            As strings para retirar do topo da pilha (na ordem inversa da
	 *            remoção)
	 * @param toPush
	 *            As strings para colocar no topo da pilha (na ordem de
	 *            inserção)
	 * @param destination
	 *            O estado de destino
	 */
	public void addTransition(String consumed, Collection<String> toPop, Collection<String> toPush, State destination) {
		addTransition(Constants.ALPHABET.getSymbol(consumed), toPop, toPush, destination);
	}

	/**
	 * Adiciona uma transição de APD.
	 *
	 * @param consumed
	 *            O símbolo consumido.
	 * @param toPop
	 *            As strings para retirar do topo da pilha (na ordem inversa da
	 *            remoção)
	 * @param toPush
	 *            As strings para colocar no topo da pilha (na ordem de
	 *            inserção)
	 * @param destination
	 *            O estado de destino
	 */
	public void addTransition(Symbol consumed, Collection<String> toPop, Collection<String> toPush, State destination) {
		if (consumed == null || destination == null) {
			throw new ValidationException("Destino ou símbolo nulo ou inexistente no alfabeto.");
		}
		if (CollectionUtils.isEmpty(toPop) || CollectionUtils.isEmpty(toPush)) {
			throw new ValidationException("Listas de caracteres de empilhamento e desempilhamento devem conter ao menos " + Alphabet.LAMBDA + ".");
		}
	}

}
