package com.eleks.academy.whoami.core.impl;

public final class StartGameAnswer extends Answer {

	public StartGameAnswer(String player) {
		super(player);
	}

	public static StartGameAnswer of(String player) {
		return new StartGameAnswer(player);
	}

}
