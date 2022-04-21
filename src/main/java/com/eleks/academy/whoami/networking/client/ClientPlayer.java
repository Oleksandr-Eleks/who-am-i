package com.eleks.academy.whoami.networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import com.eleks.academy.whoami.core.Player;

public class ClientPlayer implements Player {

	private String name;
	private BufferedReader reader;
	private PrintStream writer;

	public ClientPlayer(String name, Socket socket) throws IOException {
		this.name = name;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintStream(socket.getOutputStream());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getQuestion() {
		String question = "";

		try {
			writer.println("Ask your questinon: ");
			question = reader.readLine();
			System.out.println(name + " asks: " + question);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return question;
	}

	@Override
	public boolean answerQuestion(String question, String character) {
		String answer = "";

		try {
			writer.println("Answer other player question: " + question + " Character is: " + character);
			answer = reader.readLine();
			System.out.println(name + " answers:\n---> " + answer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer.toLowerCase().contentEquals("yes");
	}

	@Override
	public boolean isReadyForGuess() {
		String answer = "";

		try {
			writer.println("Are you ready to guess? ");
			answer = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer.toLowerCase().equals("yes");
	}

	@Override
	public String getGuess() {
		String guess = "";

		try {
			writer.println("Write your guess: ");
			guess = reader.readLine();
			System.out.println(name + " guesses: Am I " + guess);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return guess;
	}

	@Override
	public boolean answerGuess(String guess, String character) {
		String answer = "";

		try {
			writer.println("Write your answer: ");
			answer = reader.readLine();
			System.out.println(name + " answers:\n---> " + answer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer.toLowerCase().contentEquals("yes");
	}

}
