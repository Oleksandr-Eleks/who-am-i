package com.eleks.academy.whoami.networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.*;

import com.eleks.academy.whoami.core.Player;

public class ClientPlayer implements Player, AutoCloseable {

	private BufferedReader reader;
	private PrintStream writer;

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	public ClientPlayer(Socket socket) throws IOException {
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintStream(socket.getOutputStream());
	}

	@Override
	public Future<String> getName() {
		// TODO: save name for future
		return executor.submit(this::askName);
	}

	private String askName() {
		String name = "";

		try {
			writer.println("Please, name yourself.");
			name = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
	}

	@Override
	public Future<String> getQuestion() {
		return executor.submit(this::takeQuestion);
	}

	private String takeQuestion(){
		String question = "";

		try {
			writer.println("Ask your questinon: ");
			question = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return question;
	}

	@Override
	public Future<String> answerQuestion(String question, String character) {
		String answer = "";

		try {
			writer.println("Answer second player question: " + question + "Character is:"+ character);
			answer = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return CompletableFuture.completedFuture(answer);
	}

	@Override
	public Future<String> getGuess() {
		return executor.submit(this::takeGuess);
	}

	private String takeGuess(){
		String answer = "";

		try {
			writer.println("Write your guess: ");
			answer = reader.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return answer;
	}

	@Override
	public Future<Boolean> isReadyForGuess() {
		return executor.submit(this::isReadyForGuessBoolean);
	}

	public boolean isReadyForGuessBoolean(){
		String answer = "";

		try {
			writer.println("Are you ready to guess? ");
			answer = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer.equals("Yes");
	}

	@Override
	public Future<String> answerGuess(String guess, String character) {
		String answer = "";
		
		try {
			writer.println("Write your answer: ");
			answer = reader.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(answer);
	}

	@Override
	public Future<String> suggestCharacter() {
		return executor.submit(this::doSuggestCharacter);
	}

	private String doSuggestCharacter() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public void close() {
		executor.shutdown();
		try {
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
