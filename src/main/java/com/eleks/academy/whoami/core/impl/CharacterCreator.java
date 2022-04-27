package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.List;
import com.eleks.academy.whoami.core.CharactersBase;
import com.eleks.academy.whoami.core.Character;

public class CharacterCreator implements Character {
	public static int randomizer = 0;
	private List<String> characteristics = new ArrayList<>();
	
	public CharacterCreator() {
		switch (randomizer) {
			case 0 -> characteristics.addAll(CharactersBase.BATMAN.getListOfCharacteristics());
			case 1 -> characteristics.addAll(CharactersBase.ARESTOWYCH.getListOfCharacteristics());
			case 2 -> characteristics.addAll(CharactersBase.SCOOBYDOO.getListOfCharacteristics());
			case 3 -> characteristics.addAll(CharactersBase.GARFIELD.getListOfCharacteristics());
		}
		if(randomizer <= 3) {
			randomizer++;
		}else {
			randomizer = 0;
		}
		
	}
	
	public String getName() {
		return characteristics.get(0);
	}

	@Override
	public List<String> getCharacteristics() {
		return characteristics;
	}
}
