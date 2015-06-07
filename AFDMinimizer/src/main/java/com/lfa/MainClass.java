package com.lfa;

import com.lfa.parse.SyntaxMatcher;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SyntaxMatcher.validate("E(6):A,B,C,D,E,F; A(2):0,1; T(2): A->{A,B}, B->{D,C}; I(1):A; F(1):A;");
	}

}
