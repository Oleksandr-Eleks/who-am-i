package com.eleks.academy.whoami.core.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

import com.eleks.academy.whoami.core.Player;

class RandomPlayerTest {

	@Test
	void randomPlayerSuggestsCharacter() throws InterruptedException, ExecutionException, TimeoutException {
		Collection<String> characterSuggestions = List.of("A", "B");
		Player player = new RandomPlayer("P", characterSuggestions, new ArrayList<>(), new ArrayList<>());
		String character = player.suggestCharacter().get(5, TimeUnit.SECONDS);
		assertNotNull(character);
		assertTrue(characterSuggestions.contains(character));
	}

}
