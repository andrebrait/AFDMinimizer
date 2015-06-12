package com.lfa;

import com.lfa.automata.afd.AFD;
import com.lfa.exception.ValidationException;
import com.lfa.parse.InputParser;

public class MainClass {

	public static void main(String[] args) {

		try {

			// Map<String, File> fileMap =
			// ArgumentResolver.resolveArguments(args);

			String input = "E(6):A,B,C,D,E,F; A(2):0,1; T(2): A->{A,B}, B->{D,C}; I(1):A; F(1):A;";

			// try {
			// input =
			// StringUtils.join(Files.readAllLines(fileMap.get(Constants.INPUT).toPath(),
			// StandardCharsets.UTF_8), StringUtils.EMPTY);
			// } catch (IOException e) {
			// throw new
			// ValidationException("Não foi possível abrir o arquivo de entrada: "
			// + fileMap.get(Constants.INPUT).getPath());
			// }

			AFD inputAFD = InputParser.parse(input);

			int a = 3;

			// if (fileMap.containsKey(Constants.OPT_ORIGINAL)) {
			// // TODO fazer método de saída de AFD em arquivo.
			// }
			//
			// if (fileMap.containsKey(Constants.OPT_ORIGINAL)) {
			// // TODO fazer método de saída de AFD em arquivo.
			// }

		} catch (ValidationException ex) {
			System.out.println("Erro: " + ex.getMessage());
		}
	}
}
