package com.eleks.academy.whoami.networking.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.junit.jupiter.api.Timeout;

public class ClientPlayerIT {

	private static final int DURATION = 5;
	private static final TimeUnit UNIT = TimeUnit.MINUTES;

	private int randomPort() {
		return ((int) (Math.random() * (65535 - 49152)) + 49152);
	}

	@Test
	@Timeout(value = 5, unit = TimeUnit.SECONDS)
	void serverReadsClientsNameFromSocket()
			throws IOException, InterruptedException, ExecutionException, TimeoutException {

		InetAddress localHost = InetAddress.getLocalHost();
		int port = randomPort();

		CountDownLatch clientReady = new CountDownLatch(1);
		CountDownLatch clientGiveName = new CountDownLatch(1);

		try (ServerSocket server = new ServerSocket()) {
			server.bind(new InetSocketAddress(localHost, port));

			new Thread(() -> {
				try (Socket client = new Socket(localHost, port);
						PrintWriter writer = new PrintWriter(client.getOutputStream())) {

					clientReady.countDown();
					writer.println("TestUser");
					writer.flush();
					clientGiveName.await(DURATION, UNIT);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}).start();

			try (Socket client = server.accept(); 
					ClientPlayer player = new ClientPlayer(client)) {

				boolean success = clientReady.await(DURATION, UNIT);
				assertTrue(success);

				String name = player.getName().get(DURATION, UNIT);
				assertEquals("TestUser", name);
				assertNotNull(name);

				clientGiveName.countDown();
			}
		}
	}

	@Test
	@Timeout(value = 5, unit = TimeUnit.SECONDS)
	void serverReadsClientCharacterFromSocket()
			throws IOException, InterruptedException, ExecutionException, TimeoutException {

		InetAddress localHost = InetAddress.getLocalHost();
		int port = randomPort();

		CountDownLatch clientReady = new CountDownLatch(1);
		CountDownLatch clientGiveCharacter = new CountDownLatch(1);

		try (ServerSocket server = new ServerSocket()) {
			server.bind(new InetSocketAddress(localHost, port));

			new Thread(() -> {
				try (Socket client = new Socket(localHost, port);
						PrintWriter writer = new PrintWriter(client.getOutputStream())) {

					clientReady.countDown();
					writer.println("TestCharacter");
					writer.flush();
					clientGiveCharacter.await(DURATION, UNIT);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}).start();

			try (Socket client = server.accept(); 
					ClientPlayer player = new ClientPlayer(client)) {

				boolean success = clientReady.await(DURATION, UNIT);
				assertTrue(success);

				String character = player.getCharacter().get(DURATION, UNIT);
				assertEquals("TestCharacter", character);
				assertNotNull(character);

				clientGiveCharacter.countDown();
			}
		}
	}

	@Test
	@Timeout(value = 5, unit = TimeUnit.SECONDS)
	void serverReadsClientsQuestionFromSocket()
			throws IOException, InterruptedException, ExecutionException, TimeoutException {

		InetAddress localHost = InetAddress.getLocalHost();
		int port = randomPort();

		CountDownLatch clientReady = new CountDownLatch(1);
		CountDownLatch clientGiveQuestion = new CountDownLatch(1);

		try (ServerSocket server = new ServerSocket()) {
			server.bind(new InetSocketAddress(localHost, port));

			new Thread(() -> {
				try (Socket client = new Socket(localHost, port);
						PrintWriter writer = new PrintWriter(client.getOutputStream())) {

					clientReady.countDown();
					writer.println("Am i a human?");
					writer.flush();
					clientGiveQuestion.await(DURATION, UNIT);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}).start();

			try (Socket client = server.accept(); 
					ClientPlayer player = new ClientPlayer(client)) {

				boolean success = clientReady.await(DURATION, UNIT);
				assertTrue(success);

				String question = player.getQuestion().get(DURATION, UNIT);
				assertEquals("Am i a human?", question);
				assertNotNull(question);

				clientGiveQuestion.countDown();
			}
		}

	}
//
//	@Test
//	void serverReadsClientsGuessFromSocket() {
//
//	}
}
