package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.List;
import com.eleks.academy.whoami.core.CharactersBase;
import com.eleks.academy.whoami.core.Character;

public class CharacterCreator implements CharactersBase, Character {
	public static int randomizer = 0;
	
	private List<String> characteristics = new ArrayList<>();
	
	public CharacterCreator() {
		switch (randomizer)
		{
			case 0:
				setCharacter(batman);
				break;
			case 1:
				setCharacter(arestowych);
				break;
			case 2:
				setCharacter(scoobyDoo);
				break;
			case 3:
				setCharacter(garfield);
				break;
		}
		if(randomizer <= 3) {
			randomizer++;
		}else {
			randomizer = 0;
		}
		
	}
	
	public void setCharacter (List<String> characteristics) {
		
		for (int i = 0; i < characteristics.size() - 1; i++) {
            this.characteristics.add(characteristics.get(i));
        }
		
	}
	
	public String getName() {
		return characteristics.get(0);
	}
	
	

}
