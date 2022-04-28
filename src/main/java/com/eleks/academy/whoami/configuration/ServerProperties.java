package com.eleks.academy.whoami.configuration;

public class ServerProperties {
	
	private static final int NOT_ALLOWED_PORT_VALUE = 1024;

	private final int port;

	public ServerProperties(int port) {
		super();
		if (port < 0) {
			throw new IllegalArgumentException(String.format("Port value cannot be negative value: %d", port));
		}
		if (port <= NOT_ALLOWED_PORT_VALUE) {
			throw new IllegalArgumentException(String.format("Port value cannot be less than %d, but provided %d", NOT_ALLOWED_PORT_VALUE, port));
		}
		this.port = port;
	}

	public int getPort() {
		return port;
	}

}
