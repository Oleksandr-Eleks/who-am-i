package com.eleks.academy.whoami.networking.client;

import java.io.*;
import java.net.Socket;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.data.GameCharacters;
import com.eleks.academy.whoami.services.RandomizerService;

public class ClientPlayer implements Player {

	private String name;

	private String character;
	private Socket socket;
	private BufferedReader reader;
	private PrintStream writer;

	public ClientPlayer(String name, Socket socket) throws IOException {
		this.name = name;
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintStream(socket.getOutputStream());
		this.character = RandomizerService.getRandomString(GameCharacters.Characters());
	}

	@Override
	public String getName() {
		return this.name;
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
}
