package com.eleks.academy.whoami.core.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.eleks.academy.whoami.core.Player;

class RandomPlayerTest {

	@Test
	void randomPlayerSuggestsCharacter() {
		Collection<String> characterSuggestions = List.of("A", "B");
		Player player = new RandomPlayer("P", characterSuggestions, new ArrayList<>(), new ArrayList<>());
		String character = player.suggestCharacter();
		assertNotNull(character);
		assertTrue(characterSuggestions.contains(character));
	}

}
