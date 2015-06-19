package com.lfa.autotest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import com.lfa.automata.afd.AFD;
import com.lfa.automata.afd.State;
import com.lfa.automata.afd.State.Transition;

/**
 * Classe AFDAutotester. Contém os métodos necessários para inverter um AFD e
 * gerar uma palavra a partir do AFD invertido. Pode identificar loops de até
 * dois estados.
 *
 * @author Andre Brait (andrebrait@gmail.com)
 */
public class AFDAutotester {

	/**
	 * Gera a palavra a partir de um AFD qualquer. O processo envolve inverter o
	 * AFD e gerar uma palavra percorrendo o AFD invertido.
	 *
	 * @param afd
	 *            O AFD.
	 * @return A palavra gerada.
	 */
	public static String generateWord(AFD afd) {
		AFD invert = invertAFD(afd);
		StringBuilder sb = new StringBuilder();
		State next = invert.getInitialState();

		// Guarda os estados que já passaram pelos processos de ciclos.
		HashSet<String> uniCycledSources = new HashSet<>();
		HashSet<String> biCycledSources = new HashSet<>();
		do {
			Iterator<Transition> i = next.getTransitions().iterator();
			if (!i.hasNext()) {
				break;
			}

			// Escolhe uma das transições do estado aleatoriamente.
			int num = 0;
			int rand = RandomUtils.nextInt(0, next.getTransitions().size());
			while (num < rand) {
				i.next();
				num++;
			}
			Transition randomTransition = i.next();

			// Controle de loop para a geração de símbolos repetidos quando for
			// possível, mas sem que se fique gerando estes símbolos sempre.
			boolean cycledThisLoop = false;
			boolean beenUniCycled = uniCycledSources.contains(randomTransition.getSource().getName());
			boolean beenBiCycled = biCycledSources.contains(randomTransition.getSource().getName());

			// Gera símbolos repetidos se a transição for um loop simples e este
			// processo não tiver sido realizado para este estado de origem.
			if (randomTransition.getSource().equals(randomTransition.getDestination()) && !beenUniCycled) {
				sb.insert(0, StringUtils.repeat(randomTransition.getConsumed().getStr(), RandomUtils.nextInt(2, 11)));
				uniCycledSources.add(randomTransition.getSource().getName());
				cycledThisLoop = true;
			}

			// Mesmo processo que o anterior, mas este abrange a possibilidade
			// do loop envolver ir a um outro estado e então voltar ao estado de
			// origem da transição escolhida.
			if (!beenBiCycled) {
				Transition wayBack = findWayBack(randomTransition);
				if (wayBack != null) {
					sb.insert(0, StringUtils.repeat(wayBack.getConsumed().getStr() + randomTransition.getConsumed().getStr(), RandomUtils.nextInt(2, 11)));
					biCycledSources.add(randomTransition.getSource().getName());
					cycledThisLoop = true;
				}
			}
			if (!cycledThisLoop) {
				sb.insert(0, randomTransition.getConsumed().getStr());
				next = randomTransition.getDestination();
			}
			if (invert.size() == 1 || (invert.getFinalStates().contains(next) && !cycledThisLoop)) {
				break;
			}
		} while (true);
		return sb.toString();
	}

	/**
	 * Inverte um AFD. Apenas gera um novo AFD com todas as transições
	 * invertidas, estado final igual ao inicial do original e seleciona um dos
	 * finais do original para ser o inicial do invertido.
	 *
	 * @param afd
	 *            O AFD original.
	 * @return O AFD invertido.
	 */
	private static AFD invertAFD(AFD afd) {
		LinkedHashMap<String, State> invertStates = new LinkedHashMap<>();
		for (State state : afd.getStates()) {
			invertStates.put(state.getName(), new State(state.getName()));
		}
		for (State state : afd.getStates()) {
			for (Transition transition : state.getTransitions()) {
				State invertSource = invertStates.get(transition.getDestination().getName());
				State invertDestination = invertStates.get(transition.getSource().getName());
				invertSource.addReverseTransition(transition.getConsumed(), invertDestination);
			}
		}
		State reverseInitialstate = invertStates.get(afd.getFinalStates().get(RandomUtils.nextInt(0, afd.getFinalStates().size())).getName());
		AFD invertedAFD = new AFD(reverseInitialstate, invertStates.values(), Arrays.asList(invertStates.get(afd.getInitialState().getName())));
		return invertedAFD;
	}

	/**
	 * Encontra um caminho de volta ao estado original, percorrendo no máximo um
	 * outro estado.
	 *
	 * @param transition
	 *            A transição do estado de origem.
	 * @return A transição pela qual se chega novamente ao estado de origem, a
	 *         partir do estado de destino da transição original.
	 */
	private static Transition findWayBack(Transition transition) {
		for (Transition backTrans : transition.getDestination().getTransitions()) {
			if (backTrans.getDestination().equals(transition.getSource())) {
				return backTrans;
			}
		}
		return null;
	}

}
