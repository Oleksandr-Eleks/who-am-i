package com.eleks.academy.whoami.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ServerPropertiesTest {

	@Test
	void validatePort() {
		IllegalArgumentException illegalArgumentException = 
				assertThrows(IllegalArgumentException.class, () -> new ServerProperties(1024, 3));
		assertEquals("Port value cannot be 1024 or less, but provided 1024", illegalArgumentException.getMessage());
	}

	@Test
	void validateNumberOfPlayers() {
		IllegalArgumentException illegalArgumentException = 
				assertThrows(IllegalArgumentException.class, () -> new ServerProperties(1025, 1));
		assertEquals("Players value should be at least 2 or greater, but provided 1", illegalArgumentException.getMessage());
	}
}
