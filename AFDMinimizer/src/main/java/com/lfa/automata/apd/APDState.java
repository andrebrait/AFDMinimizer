package com.lfa.automata.apd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.lfa.automata.afd.State;
import com.lfa.automata.afd.Transition;
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
@ToString(callSuper = true)
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
	public void addTransition(String consumed, Collection<String> toPop, Collection<String> toPush, APDState destination) {
		addTransition(Constants.ALPHABET_SYNTAX_APD.getSymbol(consumed), toPop, toPush, destination);
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
	public void addTransition(Symbol consumed, Collection<String> toPop, Collection<String> toPush, APDState destination) {
		if (consumed == null || destination == null) {
			throw new ValidationException("Destino ou símbolo nulo ou inexistente no alfabeto.");
		}
		toPop = treat(toPop);
		toPush = treat(toPush);
		if (CollectionUtils.isEmpty(toPop) || CollectionUtils.isEmpty(toPush)) {
			throw new ValidationException("Listas de caracteres de empilhamento e desempilhamento devem conter ao menos " + Alphabet.LAMBDA + ".");
		}
		for (Transition trans : getTransitions()) {
			APDTransition apdTrans = (APDTransition) trans;
			if (apdTrans.getConsumed().equals(consumed)
					&& (apdTrans.getToPop().contains(Alphabet.LAMBDA) || toPop.contains(Alphabet.LAMBDA) || CollectionUtils.isEqualCollection(apdTrans.getToPop(), toPop))) {
				throw new ValidationException("Já existe uma transição para o mesmo símbolo com desempilhamento ambíguo." + Constants.NEWLINE + "Estado: " + this.getName()
						+ ". Símbolo da transição: " + consumed.getStr() + "." + Constants.NEWLINE + "Símbolo do empilhamento existente: " + getPopCommaSeparatedString(apdTrans.getToPop()) + "."
						+ Constants.NEWLINE + "Símbolo do empilhamento inserido: " + getPopCommaSeparatedString(toPop) + ".");
			}
		}
		getTransitions().add(new APDTransition(this, consumed, toPop, toPush, destination));
	}

	/**
	 * Trata a coleção de Strings para a colocação nas listas de empilhamento e
	 * desempilhamento do APD. Se colStr for nulo, retorna listas vazia.
	 *
	 * @param colStr
	 *            A coleção a ser tratada.
	 * @return A coleção tratada.
	 */
	private Collection<String> treat(Collection<String> colStr) {
		List<String> returnList = new ArrayList<String>();

		Optional.ofNullable(colStr).ifPresent(col -> {
			if (CollectionUtils.isEmpty(colStr)) {
				digestAndAddString(returnList, StringUtils.EMPTY);
			} else {
				col.forEach(str -> digestAndAddString(returnList, str));
			}
		});

		return returnList;
	}

	/**
	 * Digere e adiciona a string a uma coleção.
	 *
	 * @param col
	 *            A coleção de destino da String tratada.
	 * @param str
	 *            String a ser digerida e adicionada.
	 */
	private void digestAndAddString(Collection<String> col, String str) {
		str = StringUtils.defaultIfBlank(StringUtils.deleteWhitespace(str), Alphabet.LAMBDA);
		if (CollectionUtils.size(col) == 1 && col.contains(Alphabet.LAMBDA)) {
			col.clear();
		} else if (CollectionUtils.isEmpty(col) || !str.equals(Alphabet.LAMBDA)) {
			CollectionUtils.addAll(col, str.split("(?!^)"));
		}
	}

	/**
	 * Retorna a coleção de strings na forma de uma string única separada por
	 * vírgulas.
	 *
	 * @param toPop
	 *            A coleção (toPop de uma transição de APD).
	 * @return A string única.
	 */
	private String getPopCommaSeparatedString(Collection<String> toPop) {
		StringBuilder builder = new StringBuilder();
		toPop.forEach(str -> builder.append(str).append(", "));
		return builder.delete(builder.length() - 2, builder.length() - 1).toString();
	}

	@Override
	public APDState getNext() {
		return (APDState) super.getNext();
	}

	@Deprecated
	@Override
	public String consume(String str) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operação não suportada para APD.");
	}

	public String consume(String str, Stack<String> stack) {
		String firstChar = StringUtils.left(str, 1);
		for (Transition trans : getTransitions()) {
			APDTransition apdTrans = (APDTransition) trans;
			String consumed = apdTrans.getConsumed().getStr();
			if (consumed.equals(Alphabet.LAMBDA) || consumed.equals(str)) {
				popPush(apdTrans, stack);
				setNext(apdTrans.getDestination());
				return consumed.equals(Alphabet.LAMBDA) ? str : StringUtils.removeStart(str, firstChar);
			}
		}
		throw new ValidationException("Transição não encontrada para o símbolo presente." + Constants.NEWLINE + "Símbolo: " + firstChar + ".");
	}

	private void popPush(APDTransition apdTrans, Stack<String> stack) {
		apdTrans.getToPop().reverse().forEach(pop -> {
			if (!StringUtils.equals(pop, stack.pop())) {
				stack.push(pop);
				throw new ValidationException("Não foi possível desempilhar " + pop + " no estado " + getName() + Constants.NEWLINE + "Stack no momento: " + stack.toString() + ".");
			}
		});
		apdTrans.getToPush().forEach(push -> stack.push(push));
	}
}
