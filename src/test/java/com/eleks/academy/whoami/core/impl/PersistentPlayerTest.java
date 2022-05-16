package com.eleks.academy.whoami.core.impl;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersistentPlayerTest {

	@Test
	void allowToSuggestCharacterOnlyOnce() {
		PersistentPlayer player = new PersistentPlayer("PLayerName");
		Future<String> character = player.suggestCharacter();
		assertFalse(character.isDone());
		player.setCharacter("character");
		assertTrue(character.isDone());
		assertThrows(IllegalStateException.class, () -> player.setCharacter("character"));
	}
}
