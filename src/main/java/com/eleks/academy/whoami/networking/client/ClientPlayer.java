package com.eleks.academy.whoami.networking.client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.data.GameCharacters;
import com.eleks.academy.whoami.services.RandomizerService;

public class ClientPlayer implements Player, AutoCloseable {

<<<<<<< HEAD
	private String name;

	private String character;
	private Socket socket;
	private BufferedReader reader;
	private PrintStream writer;
=======
	private final BufferedReader reader;
	private final PrintStream writer;
	private final Socket socket;
>>>>>>> fetch/feature/lecture-8

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	public ClientPlayer(Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintStream(socket.getOutputStream());
		this.character = RandomizerService.getRandomString(GameCharacters.Characters());
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
	public String getQuestion() {
		String question = "";

		try {
			writer.println("Ask your question: ");
			question = reader.readLine();
			System.out.println(String.format("%s asks: %s\tCharacter is: %s", name, question, character));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return question;
	}

	@Override
	public String answerQuestion(String question, String character) {
		String answer = "";
		
		try {
			writer.println(String.format("Answer the question: %s\tCharacter is: %s", question, character));
			answer = reader.readLine();
			System.out.println(name + " Answers: " + answer + "\t");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}

	@Override
	public String getGuess() {
		String answer = "";

		try {
			writer.println("Write your guess: ");
			answer = reader.readLine();
			System.out.println(String.format("%s Am I %s?\tCharacter is: ", name, answer, character));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}

	@Override
	public boolean isReadyForGuess() {
		String answer = "";
		
		try {
			writer.println("Are you ready to guess? ");
			answer = reader.readLine();
			if (answer.equalsIgnoreCase("yes")){
				System.out.println(name + " ready for guess!");
			} else {
				System.out.println(name + " not ready for guessing...");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer.equalsIgnoreCase("Yes") ? true : false;
	}

	@Override
	public String answerGuess(String guess, String character) {
		String answer = "";
		
		try {
			writer.println("Write your answer: ");
			answer = reader.readLine();
			System.out.println(String.format("%s. Answers: %s\t", name, answer));
		} catch (IOException e) {

			e.printStackTrace();
		}
		return answer;
	}
<<<<<<< HEAD
=======

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
		close(writer);
		close(reader);
		close(socket);
	}
	
	private void close(AutoCloseable closeable) {
		try {
			closeable.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

>>>>>>> fetch/feature/lecture-8
}
