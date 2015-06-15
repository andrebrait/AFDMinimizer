package com.lfa.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import com.lfa.automata.afd.AFD;
import com.lfa.automata.afd.State;
import com.lfa.automata.afd.State.Transition;
import com.lfa.constants.Constants;

/**
 * Classe OutputPrinter. Contém métodos para gerar uma saída com base em um
 * objeto AFD.
 */
public class OutputPrinter {

	/**
	 * Gera um arquivo de saída correspondente ao AFD com o formato apropriado
	 * para uso no programa Graphviz.
	 *
	 * @param afd
	 *            O AFD.
	 * @param file
	 *            O arquivo de saída.
	 * @param name
	 *            O nome do AFD.
	 * @param charset
	 *            O charset de saída.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void printToFile(AFD afd, File file, String name, Charset charset) throws IOException {
		PrintWriter pw = new PrintWriter(file, charset.name());
		BufferedWriter bw = new BufferedWriter(pw);
		bw.write("digraph " + Constants.QUOTE + name + Constants.QUOTE + " {");
		bw.newLine();
		bw.write(Constants.TAB + "_nil [style=" + Constants.QUOTE + "invis" + Constants.QUOTE + "];");
		bw.newLine();
		bw.write(Constants.TAB + "_nil  -> " + afd.getInitialState().getName() + " [label=" + Constants.QUOTE + Constants.QUOTE + "];");
		bw.newLine();
		for (State state : afd.getFinalStates()) {
			bw.write(Constants.TAB + state.getName() + " [peripheries=2];");
			bw.newLine();
		}
		for (State state : afd.getStates()) {
			for (Transition trans : state.getTransitions()) {
				bw.write(Constants.TAB + state.getName() + " -> " + trans.getDestination().getName() + " [label=" + trans.getConsumed().getStr() + "];");
				bw.newLine();
			}
		}
		bw.write("}");
		bw.close();
	}

}
