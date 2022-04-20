package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.List;
import com.eleks.academy.whoami.core.CharactersBase;
import com.eleks.academy.whoami.core.Character;

public class CharacterCreator implements CharactersBase, Character {
	public static int randomizer = 0;
	
	private List<String> characteristics = new ArrayList<>();
	
	public CharacterCreator() {
		switch (randomizer) {
			case 0 -> this.characteristics.addAll(batman);
			case 1 -> this.characteristics.addAll(arestowych);
			case 2 -> this.characteristics.addAll(scoobyDoo);
			case 3 -> this.characteristics.addAll(garfield);
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
