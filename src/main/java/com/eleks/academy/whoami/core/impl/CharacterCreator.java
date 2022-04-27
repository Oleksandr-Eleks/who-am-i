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
			case 0 -> characteristics.addAll(List.of(
					Batman.NAME.getStr(), Batman.CREATURE.getStr(),
					Batman.REALITY.getStr(), Batman.GENDER.getStr(),
					Batman.AGE.getStr(), Batman.HAIRCOLOR.getStr()));
			case 1 -> characteristics.addAll(List.of(
					Arestowych.NAME.getStr(), Arestowych.CREATURE.getStr(),
					Arestowych.REALITY.getStr(), Arestowych.GENDER.getStr(),
					Arestowych.AGE.getStr(), Arestowych.HAIRCOLOR.getStr()));
			case 2 -> characteristics.addAll(List.of(
					ScoobyDoo.NAME.getStr(), ScoobyDoo.CREATURE.getStr(),
					ScoobyDoo.REALITY.getStr(), ScoobyDoo.GENDER.getStr(),
					ScoobyDoo.AGE.getStr(), ScoobyDoo.HAIRCOLOR.getStr()));
			case 3 -> characteristics.addAll(List.of(
					Garfield.NAME.getStr(), Garfield.CREATURE.getStr(),
					Garfield.REALITY.getStr(), Garfield.GENDER.getStr(),
					Garfield.AGE.getStr(), Garfield.HAIRCOLOR.getStr()));
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
