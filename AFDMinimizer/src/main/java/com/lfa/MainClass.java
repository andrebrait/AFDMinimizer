package com.lfa;

import java.io.File;
import java.util.Map;

import com.lfa.argsolve.ArgumentResolver;
import com.lfa.constants.Constants;
import com.lfa.parse.SyntaxMatcher;

public class MainClass {

	public static void main(String[] args) {

		Map<String, File> fileMap = ArgumentResolver.resolveArguments(args);

		File inputFile = fileMap.get(Constants.INPUT);

		if (fileMap.containsKey(Constants.OPT_ORIGINAL)) {
			// TODO fazer método de saída de AFD em arquivo.
		}

		if (fileMap.containsKey(Constants.OPT_ORIGINAL)) {
			// TODO fazer método de saída de AFD em arquivo.
		}

		// TODO Auto-generated method stub
		SyntaxMatcher.validate("E(6):A,B,C,D,E,F; A(2):0,1; T(2): A->{A,B}, B->{D,C}; I(1):A; F(1):A;");
	}

}
