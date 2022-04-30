package com.eleks.academy.whoami.configuration;

public class ServerProperties {

	private static final int NOT_ALLOWED_PORT_VALUE = 1024;

	private final int port;
	private final int players;

	public ServerProperties(int port, int players) {
		super();
		if (port < 0) {
			throw new IllegalArgumentException(String.format("Port value cannot be negative value: %d", port));
		}
		if (port <= NOT_ALLOWED_PORT_VALUE) {
			throw new IllegalArgumentException(
					String.format("Port value cannot be less than %d, but provided %d", NOT_ALLOWED_PORT_VALUE, port));
		}
		if (players < 0) {
			throw new IllegalArgumentException(String.format("Players value cannot be negative value: %d", players));
		}
		if (players <= 2) {
			throw new IllegalArgumentException(
					String.format("Players value should be greater then %d, but provided %d", 2, players));
		}
		this.port = port;
		this.players = players;
	}

	public int getPort() {
		return port;
	}

	public int getPlayers() {
		return players;
	}

}
