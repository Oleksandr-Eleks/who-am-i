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

class ClientPlayerIT {

	private static final int DURATION = 5;
	private static final TimeUnit UNIT = TimeUnit.SECONDS;

	private int randomPort() {
		return ((int) (Math.random() * (65535 - 49152)) + 49152);
	}

	@Test
	@Timeout(value = 5)
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

			try (Socket client = server.accept(); ClientPlayer player = new ClientPlayer(client)) {

				boolean success = clientReady.await(DURATION, UNIT);
				assertTrue(success);

				String name = player.askName().get(DURATION, UNIT);
				assertEquals("TestUser", name);
				assertNotNull(name);

				clientGiveName.countDown();
			}
		}
	}

	@Test
	@Timeout(value = 5)
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
					writer.println("random");
					writer.println("TestCharacter");
					writer.flush();
					clientGiveCharacter.await(DURATION, UNIT);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}).start();

			try (Socket client = server.accept(); ClientPlayer player = new ClientPlayer(client)) {

				boolean success = clientReady.await(DURATION, UNIT);
				assertTrue(success);

				String character = player.askCharacter().get(DURATION, UNIT);
				assertEquals("TestCharacter", character);
				assertNotNull(character);

				clientGiveCharacter.countDown();
			}
		}
	}

	@Test
	@Timeout(value = 5)
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
					writer.println("Am i a superhuman?");
					writer.println("Am i a superhuman?");
					writer.flush();
					clientGiveQuestion.await(DURATION, UNIT);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}).start();

			try (Socket client = server.accept(); ClientPlayer player = new ClientPlayer(client)) {

				boolean success = clientReady.await(DURATION, UNIT);
				assertTrue(success);

				String question = player.askQuestion().get(DURATION, UNIT);
				assertEquals("Am i a superhuman?", question);
				assertNotNull(question);

				clientGiveQuestion.countDown();
			}
		}

	}

	@Test
	@Timeout(value = 5)
	void serverReadsClientsGuessFromSocket()
			throws IOException, InterruptedException, ExecutionException, TimeoutException {

		InetAddress localHost = InetAddress.getLocalHost();
		int port = randomPort();

		CountDownLatch clientReady = new CountDownLatch(1);
		CountDownLatch clientGiveGuess = new CountDownLatch(1);

		try (ServerSocket server = new ServerSocket()) {
			server.bind(new InetSocketAddress(localHost, port));

			new Thread(() -> {
				try (Socket client = new Socket(localHost, port);
						PrintWriter writer = new PrintWriter(client.getOutputStream())) {

					clientReady.countDown();
					writer.println("Am i a SeniorDeveloper?");
					writer.println("Am i a SeniorDeveloper?");
					writer.flush();
					clientGiveGuess.await(DURATION, UNIT);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}).start();

			try (Socket client = server.accept(); ClientPlayer player = new ClientPlayer(client)) {

				boolean success = clientReady.await(DURATION, UNIT);
				assertTrue(success);

				String guess = player.askQuestion().get(DURATION, UNIT);
				assertEquals("Am i a SeniorDeveloper?", guess);
				assertNotNull(guess);

				clientGiveGuess.countDown();
			}
		}
	}

	@Test
	@Timeout(value = 5)
	void serverReadsClientsQuestionAnswerFromSocket()
			throws IOException, InterruptedException, ExecutionException, TimeoutException {

		String playerName = "Player22654";
		String question = "am i a human?";
		String character = "Car";

		InetAddress localHost = InetAddress.getLocalHost();
		int port = randomPort();

		CountDownLatch clientReady = new CountDownLatch(1);
		CountDownLatch clientGiveQuestionAnswer = new CountDownLatch(1);

		try (ServerSocket server = new ServerSocket()) {
			server.bind(new InetSocketAddress(localHost, port));

			new Thread(() -> {
				try (Socket client = new Socket(localHost, port);
						PrintWriter writer = new PrintWriter(client.getOutputStream())) {

					clientReady.countDown();
					writer.println("No");
					writer.println("No");
					writer.flush();
					clientGiveQuestionAnswer.await(DURATION, UNIT);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}).start();

			try (Socket client = server.accept(); ClientPlayer player = new ClientPlayer(client)) {

				boolean success = clientReady.await(DURATION, UNIT);
				assertTrue(success);

				String answer = player.answerQuestion(playerName, question, character).get(DURATION, UNIT);
				assertEquals("no", answer.toLowerCase());
				assertNotNull(answer);

				clientGiveQuestionAnswer.countDown();
			}
		}
	}

	@Test
	@Timeout(value = 5)
	void serverReadsClientsGuessAnswerFromSocket()
			throws IOException, InterruptedException, ExecutionException, TimeoutException {

		String playerName = "Player22654";
		String guess = "am i byke?";
		String character = "Car";

		InetAddress localHost = InetAddress.getLocalHost();
		int port = randomPort();

		CountDownLatch clientReady = new CountDownLatch(1);
		CountDownLatch clientGiveGuessAnswer = new CountDownLatch(1);

		try (ServerSocket server = new ServerSocket()) {
			server.bind(new InetSocketAddress(localHost, port));

			new Thread(() -> {
				try (Socket client = new Socket(localHost, port);
						PrintWriter writer = new PrintWriter(client.getOutputStream())) {

					clientReady.countDown();
					writer.println("No");
					writer.println("No");
					writer.flush();
					clientGiveGuessAnswer.await(DURATION, UNIT);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}).start();

			try (Socket client = server.accept(); ClientPlayer player = new ClientPlayer(client)) {

				boolean success = clientReady.await(DURATION, UNIT);
				assertTrue(success);

				String answer = player.answerGuess(playerName, guess, character).get(DURATION, UNIT);
				assertEquals("no", answer.toLowerCase());
				assertNotNull(answer);

				clientGiveGuessAnswer.countDown();
			}
		}
	}

	@Test
	@Timeout(value = 5)
	void serverReadsClientIsReadyForGuessFromSocket()
			throws IOException, InterruptedException, ExecutionException, TimeoutException {

		InetAddress localHost = InetAddress.getLocalHost();
		int port = randomPort();

		CountDownLatch clientReady = new CountDownLatch(1);
		CountDownLatch clientReadyToGuess = new CountDownLatch(1);

		try (ServerSocket server = new ServerSocket()) {
			server.bind(new InetSocketAddress(localHost, port));

			new Thread(() -> {
				try (Socket client = new Socket(localHost, port);
						PrintWriter writer = new PrintWriter(client.getOutputStream())) {

					clientReady.countDown();
					writer.println("No");
					writer.println("No");
					writer.flush();
					clientReadyToGuess.await(DURATION, UNIT);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}).start();

			try (Socket client = server.accept(); ClientPlayer player = new ClientPlayer(client)) {

				boolean success = clientReady.await(DURATION, UNIT);
				assertTrue(success);

				String answer = player.isReadyForGuess().get(DURATION, UNIT).toLowerCase();
				assertEquals("no", answer);
				assertNotNull(answer);

				clientReadyToGuess.countDown();
			}
		}
	}
}
