package com.eleks.academy.whoami.core;

import java.util.List;

public enum CharactersBase {
	BATMAN(List.of("Batman", "human", "fiction", "male", "adult", "black")),
	ARESTOWYCH(List.of("Arestowych", "human", "real", "male", "adult", "black")),
	SCOOBYDOO(List.of("Scooby Doo", "dog", "fiction", "male", "adult", "brown")),
	GARFIELD(List.of("Garfield", "cat", "fiction", "male", "adult", "red"));

	private List <String> listOfCharacteristics;

	CharactersBase(List<String> listOfCharacteristics) {
		this.listOfCharacteristics = listOfCharacteristics;
	}

	public List<String> getListOfCharacteristics() {
		return this.listOfCharacteristics;
	}
}
