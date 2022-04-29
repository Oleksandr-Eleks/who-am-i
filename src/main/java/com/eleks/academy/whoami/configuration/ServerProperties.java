package com.eleks.academy.whoami.configuration;

public class ServerProperties {

	private static final int RESERVED_PORTS = 1024;
	private final int port;
	private final int players;
	
	public ServerProperties(int port, int players) {
		super();
		if (port <= RESERVED_PORTS) {
			throw new IllegalArgumentException(
					String.format("Port value cannot be %d or less, but provided %d", RESERVED_PORTS, port));
		}
		if (players < 2) {
			throw new IllegalArgumentException(
					String.format("Players value should be at least %d or greater, but provided %d", 2, players));
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
