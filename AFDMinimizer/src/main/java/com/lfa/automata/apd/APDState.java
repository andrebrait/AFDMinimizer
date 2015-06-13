package com.lfa.automata.apd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.lfa.automata.afd.State;
import com.lfa.automata.afd.Transition;
import com.lfa.constants.Alphabet;
import com.lfa.constants.Constants;
import com.lfa.constants.Symbol;
import com.lfa.exception.ValidationException;
import com.lfa.exception.ValidationException.ErrorType;

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
	public void addTransition(String consumed, String toPop, String toPush, APDState destination) {
		addTransition(Constants.ALPHABET_APD.getSymbol(consumed), toPop, toPush, destination);
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
	public void addTransition(Symbol consumed, String toPop, String toPush, APDState destination) {
		addTransition(consumed, Arrays.asList(StringUtils.defaultString(toPop, StringUtils.EMPTY)), Arrays.asList(StringUtils.defaultString(toPush, StringUtils.EMPTY)), destination);
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
		addTransition(Constants.ALPHABET_APD.getSymbol(consumed), toPop, toPush, destination);
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
			throw new ValidationException(ErrorType.APD, "Destino ou símbolo nulo ou inexistente no alfabeto.");
		}
		toPop = treat(toPop);
		toPush = treat(toPush);
		for (Transition trans : getTransitions()) {
			APDTransition apdTrans = (APDTransition) trans;
			if (apdTrans.getConsumed().equals(consumed)
					&& (apdTrans.getToPop().contains(Alphabet.LAMBDA) || toPop.contains(Alphabet.LAMBDA) || CollectionUtils.isEqualCollection(apdTrans.getToPop(), toPop))) {
				throw new ValidationException(ErrorType.APD, "Já existe uma transição para o mesmo símbolo com desempilhamento ambíguo." + Constants.NEWLINE + "Estado: " + this.getName()
						+ ". Símbolo da transição: " + consumed.getStr() + "." + Constants.NEWLINE + "Símbolo do empilhamento existente: " + apdTrans.getToPop() + "." + Constants.NEWLINE
						+ "Símbolo do empilhamento inserido: " + apdTrans.getToPop() + ".");
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

		if (CollectionUtils.isEmpty(colStr)) {
			digestAndAddString(returnList, StringUtils.EMPTY);
		} else {
			for (String str : colStr) {
				digestAndAddString(returnList, str);
			}
		}

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

		if (!(col.contains(Alphabet.LAMBDA) || CollectionUtils.containsAll(Constants.ALPHABET_APD_STACK.getSymbolMap().keySet(), col))) {
			throw new ValidationException(ErrorType.APD, "Símbolo não presente no alfabeto de pilha: " + CollectionUtils.removeAll(col, Constants.ALPHABET_APD_STACK.getSymbolMap().keySet()) + ".");
		}
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

	/**
	 * Consome a string fornecida, empilhando e desempilhando caracteres
	 * correspondentes.
	 *
	 * @param str
	 *            A string
	 * @param stack
	 *            A pilha do autômato
	 * @return A string menos o primeiro caractere, se o símbolo consumido não
	 *         for Lambda.
	 */
	public String consume(String str, Deque<String> stack) {
		String firstChar = StringUtils.left(str, 1);
		for (Transition trans : getTransitions()) {
			APDTransition apdTrans = (APDTransition) trans;
			String consumed = apdTrans.getConsumed().getStr();
			if ((consumed.equals(Alphabet.LAMBDA) || consumed.equals(firstChar)) && checkToPop(apdTrans, stack)) {
				popPush(apdTrans, stack);
				setNext(apdTrans.getDestination());
				return consumed.equals(Alphabet.LAMBDA) ? str : StringUtils.removeStart(str, firstChar);
			}
		}
		throw new ValidationException(ErrorType.APD, "Transição não encontrada para o símbolo presente no estado " + getName() + "." + Constants.NEWLINE + "Símbolo: " + firstChar + ". String: " + str
				+ "." + Constants.NEWLINE + "Stack: " + stack + ".");
	}

	/**
	 * Verifica se os caracteres de desempilhamento de uma transição estão no
	 * stack.
	 *
	 * @param apdTrans
	 *            A transição do APD.
	 * @param stack
	 *            A pilha do autômato.
	 * @return true, se for possível desempilhar os caracteres necessários.
	 */
	private boolean checkToPop(APDTransition apdTrans, Deque<String> stack) {
		if (apdTrans.getToPop().contains(Alphabet.LAMBDA)) {
			return true;
		}
		boolean check = CollectionUtils.size(stack) >= apdTrans.getToPop().size();
		Iterator<String> stackIter = stack.iterator();
		Iterator<String> popIter = apdTrans.getToPop().reverse().iterator();
		while (popIter.hasNext() && check) {
			String pop = popIter.next();
			check = check && pop.equals(stackIter.next());
		}
		return check;
	}

	/**
	 * Desempilha e empilha os caracteres necessários para percorrer uma
	 * transição de estado.
	 *
	 * @param apdTrans
	 *            A transição.
	 * @param stack
	 *            A pilha do autômato.
	 */
	private void popPush(APDTransition apdTrans, Deque<String> stack) {
		for (String pop : apdTrans.getToPop().reverse()) {
			if (pop.equals(Alphabet.LAMBDA)) {
				break;
			}
			String str = CollectionUtils.isEmpty(stack) ? null : stack.pop();
			if (!StringUtils.equals(pop, str)) {
				throw new ValidationException(ErrorType.APD, "Não foi possível desempilhar " + pop + " no estado " + getName() + Constants.NEWLINE + "Stack no momento: " + str + StringUtils.SPACE
						+ stack.toString() + ".");
			}
		}
		for (String push : apdTrans.getToPush()) {
			if (!push.equals(Alphabet.LAMBDA)) {
				stack.push(push);
			}
		}
	}
}
