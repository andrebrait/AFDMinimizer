package com.lfa.apd;

import com.lfa.afd.State;

public class APDState extends State {

	public APDState(String name) {
		super(name);
	}

	public APDState(String name, boolean finalState) {
		super(name, finalState);
	}

}
