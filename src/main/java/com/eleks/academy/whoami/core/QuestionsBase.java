package com.eleks.academy.whoami.core;

import java.util.List;

public enum QuestionsBase {
	CREATURE(List.of("human", "cat", "dog")),
	REALITY(List.of("real", "fiction")),
	GENDER(List.of("male", "female")),
	AGE(List.of("adult")),
	HAIRCOLOR(List.of("red", "black", "brown"));

	private List <String> characteristics;

	QuestionsBase(List<String> characteristics) {
		this.characteristics = characteristics;
	}

	public List<String> getCharacteristics() {
		return this.characteristics;
	}
}
