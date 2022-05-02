package com.eleks.academy.whoami.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ServerPropertiesTest {

	@Test
	void validatePort() {
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> new ServerProperties(-100, 3));
		assertEquals("Port value cannot be negative value: -100", illegalArgumentException.getMessage());
		illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> new ServerProperties(1024, 3));
		assertEquals("Port value cannot be less than 1024, but provided 1024", illegalArgumentException.getMessage());
	}

	@Test
	void validatePlayers() {
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> new ServerProperties(1025, -2));
		assertEquals("Players value cannot be negative value: -2", illegalArgumentException.getMessage());
		illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> new ServerProperties(1025, 2));
		assertEquals("Players value should be greater then 2, but provided 2", illegalArgumentException.getMessage());
	}

}
