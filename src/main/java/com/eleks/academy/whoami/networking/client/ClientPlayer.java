package com.eleks.academy.whoami.networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import com.eleks.academy.whoami.core.Player;

public class ClientPlayer implements Player {

	private BufferedReader reader;
	private PrintStream writer;

	public ClientPlayer(Socket socket) throws IOException {
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintStream(socket.getOutputStream());
	}

	@Override
	public String getName() {
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
			writer.println("Ask your questinon: ");
			question = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return question;
	}

	@Override
	public String answerQuestion(String question, String character) {
		String answer = "";
		
		try {
			writer.println("Answer second player question: " + question + "Character is:"+ character);
			answer = reader.readLine();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return answer.equals("Yes") ? true : false;
	}

	@Override
	public String answerGuess(String guess, String character) {
		String answer = "";
		
		try {
			writer.println("Write your answer: ");
			answer = reader.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return answer;
	}

	@Override
	public String suggestCharacter() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

}
