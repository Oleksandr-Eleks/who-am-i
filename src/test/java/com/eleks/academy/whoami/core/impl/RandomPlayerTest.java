package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
