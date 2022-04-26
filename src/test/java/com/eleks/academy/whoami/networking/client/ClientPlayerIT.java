package com.eleks.academy.whoami.networking.client;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.eleks.academy.whoami.core.Player;

class ClientPlayerIT {

	@Test
	void clientReadsCharacterFromSocket() throws IOException, InterruptedException {
		InetAddress localHost = InetAddress.getLocalHost();
		int port = randomPort();

		CountDownLatch clientReady = new CountDownLatch(1);

		try (ServerSocket server = new ServerSocket()) {
			server.bind(new InetSocketAddress(localHost, port));

			new Thread(() -> {
				try (Socket client = new Socket(localHost, port);
						PrintWriter writer = new PrintWriter(client.getOutputStream())) {
					writer.write("test character");
					writer.flush();
					clientReady.countDown();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}).start();

			try (Socket client = server.accept()) {
				// TODO: refactor test to always fail after 5 seconds
				Player player = new ClientPlayer(client);
				boolean success = clientReady.await(5, TimeUnit.SECONDS);
				assertTrue(success);
				String character = player.suggestCharacter();
				assertEquals("test character", character);
			}
		}
	}

	@Test
	void clientReadsPlayersNameFromSocket() throws IOException, InterruptedException {
		InetAddress localHost = InetAddress.getLocalHost();
		int port = randomPort();

		CountDownLatch clientReady = new CountDownLatch(1);

		try (ServerSocket server = new ServerSocket()) {
			server.bind(new InetSocketAddress(localHost, port));

			new Thread(() -> {
				try (Socket client = new Socket(localHost, port);
						PrintWriter writer = new PrintWriter(client.getOutputStream())) {
					writer.write("Player");
					writer.flush();
					clientReady.countDown();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}).start();

			try (Socket client = server.accept()) {
				// TODO: refactor test to always fail after 5 seconds
				Player player = new ClientPlayer(client);
				boolean success = clientReady.await(5, TimeUnit.SECONDS);
				assertTrue(success);
				String character = player.getName();
				assertEquals("Player", character);
			}
		}
	}

	private int randomPort() {
		return ((int) (Math.random() * (65535 - 49152)) + 49152);
	}

}
