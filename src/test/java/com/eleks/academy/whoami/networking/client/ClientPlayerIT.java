package com.eleks.academy.whoami.networking.client;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

class ClientPlayerIT {

	@Test
	void clientReadsCharacterFromSocket() throws IOException, InterruptedException, ExecutionException, TimeoutException {
		InetAddress localHost = InetAddress.getLocalHost();
		int port = randomPort();

		CountDownLatch clientReady = new CountDownLatch(1);

		try (ServerSocket server = new ServerSocket()) {
			server.bind(new InetSocketAddress(localHost, port));

			new Thread(() -> {
				try (Socket client = new Socket(localHost, port);
						PrintWriter writer = new PrintWriter(client.getOutputStream())) {
					writer.println("test character");
					writer.flush();
					clientReady.countDown();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}).start();

			try (Socket client = server.accept();
					ClientPlayer player = new ClientPlayer(client)) {
				// TODO: refactor test to always fail after 5 seconds
				boolean success = clientReady.await(5, TimeUnit.SECONDS);
				assertTrue(success);
				String character = player.suggestCharacter().get(5, TimeUnit.SECONDS);
				assertEquals("test character", character);
			}
		}
	}

	@Test
	void clientReadsPlayersNameFromSocket() throws IOException, InterruptedException, ExecutionException, TimeoutException {
		InetAddress localHost = InetAddress.getLocalHost();
		int port = randomPort();

		CountDownLatch clientReady = new CountDownLatch(1);
		CountDownLatch nameAppeared = new CountDownLatch(1);

		try (ServerSocket server = new ServerSocket()) {
			server.bind(new InetSocketAddress(localHost, port));

			new Thread(() -> {
				try (Socket client = new Socket(localHost, port);
						PrintWriter writer = new PrintWriter(client.getOutputStream())) {
					clientReady.countDown();
					writer.println("Player");
					writer.flush();
					nameAppeared.await(5, TimeUnit.SECONDS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}).start();

			try (Socket client = server.accept();
					ClientPlayer player = new ClientPlayer(client)) {
				// TODO: refactor test to always fail after 5 seconds
				boolean success = clientReady.await(5, TimeUnit.SECONDS);
				assertTrue(success);
				String character = player.getName().get(5, TimeUnit.SECONDS);
				assertEquals("Player", character);
				nameAppeared.countDown();
			}
		}
	}

	private int randomPort() {
		return ((int) (Math.random() * (65535 - 49152)) + 49152);
	}

}
