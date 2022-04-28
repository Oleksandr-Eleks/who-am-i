package com.eleks.academy.whoami.configuration;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ServerPropertiesTest {

	@Test
	void validatePort() {
		assertThrows(IllegalArgumentException.class, () -> new ServerProperties(-100));
		assertThrows(IllegalArgumentException.class, () -> new ServerProperties(1024));
	}

}
