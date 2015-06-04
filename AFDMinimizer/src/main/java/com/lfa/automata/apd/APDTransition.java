package com.lfa.automata.apd;

import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.google.common.collect.ImmutableList;
import com.lfa.automata.afd.Transition;
import com.lfa.constants.Alphabet.Symbol;

/**
 * Classe APDTransition. Trata uma transição do APD.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class APDTransition extends Transition {

	private final ImmutableList<String> toPop;
	private final ImmutableList<String> toPush;

	/**
	 * Instancia uma nova transição de APD.
	 *
	 * @param source
	 *            O estado de origem.
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
	protected APDTransition(APDState source, Symbol consumed, Collection<String> toPop, Collection<String> toPush, APDState destination) {
		super(source, consumed, destination);
		this.toPop = ImmutableList.<String> builder().addAll(toPop).build();
		this.toPush = ImmutableList.<String> builder().addAll(toPush).build();
	}

}
