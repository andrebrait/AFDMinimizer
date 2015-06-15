package com.lfa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.mozilla.universalchardet.UniversalDetector;

import com.lfa.argsolve.ArgumentResolver;
import com.lfa.automata.afd.AFD;
import com.lfa.constants.Constants;
import com.lfa.exception.ValidationException;
import com.lfa.exception.ValidationException.ErrorType;
import com.lfa.minimize.AFDMinimizer;
import com.lfa.output.OutputPrinter;
import com.lfa.parse.InputParser;

/**
 * Classe MainClass. Inicia a execução do programa.
 */
public class MainClass {

	public static void main(String[] args) {

		// "E(6):A,B,C,D,E,F; A(2):0,1; T(6): A->{A,B}, B->{D,C}, C -> {A, B}, D -> {E, F}, E -> {D, C}, F -> {E,F}; I(1):A; F(1):A;"

		try {

			Map<String, File> fileMap = ArgumentResolver.resolveArguments(args);

			File inputFile = null;
			String input = null;
			Charset detectedCharSet = null;

			try {
				inputFile = fileMap.get(Constants.INPUT);
				detectedCharSet = detectCharSet(inputFile);
				input = StringUtils.join(Files.readAllLines(inputFile.toPath(), detectedCharSet), StringUtils.EMPTY);

				AFD inputAFD = InputParser.parse(input);
				AFD outputAFD = AFDMinimizer.minimize(inputAFD);

				if (fileMap.containsKey(Constants.OPT_ORIGINAL)) {
					OutputPrinter.printToFile(inputAFD, fileMap.get(Constants.OPT_ORIGINAL), "AFD", detectedCharSet);
				}

				if (fileMap.containsKey(Constants.OPT_MINIMIZED)) {
					OutputPrinter.printToFile(outputAFD, fileMap.get(Constants.OPT_MINIMIZED), "AFD Minimizado", detectedCharSet);
				}
				if (inputAFD.size() == outputAFD.size()) {
					System.out.println("AFD já era mínimo.");
				} else {
					System.out.println("AFD não era mínimo e foi minimizado.");
				}
			} catch (IOException e) {
				throw new ValidationException(ErrorType.OTHER, "Não foi possível abrir o arquivo de entrada ou um dos de saída");
			}

		} catch (ValidationException ex) {
			System.out.println("Erro: " + ex.getMessage());
		}
	}

	/**
	 * Detecta o charset do arquivo.
	 *
	 * @param file
	 *            O arquivo
	 * @return O charset
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static Charset detectCharSet(File file) throws IOException {
		UniversalDetector charSetDetector = new UniversalDetector(null);
		byte[] buf = new byte[4096];
		FileInputStream fis = new FileInputStream(file);
		int nread;
		while ((nread = fis.read(buf)) > 0 && !charSetDetector.isDone()) {
			charSetDetector.handleData(buf, 0, nread);
		}
		charSetDetector.dataEnd();
		String encoding = charSetDetector.getDetectedCharset();
		charSetDetector.reset();
		fis.close();
		return encoding != null && Charset.isSupported(encoding) ? Charset.forName(encoding) : StandardCharsets.UTF_8;
	}
}
