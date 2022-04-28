package com.eleks.academy.whoami.networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
<<<<<<< Updated upstream
=======
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
>>>>>>> Stashed changes

import com.eleks.academy.whoami.core.Player;

public class ClientPlayer implements Player {

<<<<<<< Updated upstream
	private String name;
	private Socket socket;
	private BufferedReader reader;
	private PrintStream writer;

	public ClientPlayer(String name, Socket socket) throws IOException {
		this.name = name;
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintStream(socket.getOutputStream());
	}

	@Override
	public String getName() {
		return this.name;
=======
    private BufferedReader reader;
    private PrintStream writer;
    private String name = "";
    private Socket socket;
    private String question;
    private String character;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ClientPlayer(Socket socket) throws IOException {
	this.socket = socket;
	this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
	this.writer = new PrintStream(this.socket.getOutputStream());
    }

    @Override
    public Future<String> getName() {
	return executor.submit(this::askName);
    }

    @Override
    public Future<String> getQuestion() {
	return executor.submit(this::askQueston);
    }

    @Override
    public Future<String> answerQuestion(String question, String character) {
	this.question = question;
	this.character = character;
	return executor.submit(this::doAnswerQuestion);
    }

    @Override
    public Future<String> getGuess() {
	return executor.submit(this::askGuess);
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
    public Future<String> answerGuess(String guess, String character) {
	return executor.submit(this::doAnswerGuess);
    }

    @Override
    public Future<String> suggestCharacter() {
	return executor.submit(this::doSuggestCharacter);
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

    private String doSuggestCharacter() {
	try {
	    return reader.readLine();
	} catch (IOException e) {
	    e.printStackTrace();
	    return "";
>>>>>>> Stashed changes
	}
    }

<<<<<<< Updated upstream
	@Override
	public String getQuestion() {
		String question = "";

		try {

			this.writer.println("Ask your questinon: ");
			System.out.println("Ask your questinon: ");

			question = reader.readLine();

			System.out.println("Player " + this.name + ": " + question);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return question;
	}

	@Override
	public Socket getPlayerSocket() {
		return this.socket;
	}

	@Override
	public String getGuess() {
		String answer = "";

		try {

			this.writer.println("Write your guess: ");
			System.out.println("Write your guess: ");

			answer = reader.readLine();

			System.out.println("Player " + this.name + ": " + answer);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}

	@Override
	public String answerQuestion(String question, String character) {
		String answer = "";

		try {

			this.writer.println("Answer" + this.name + " player question: " + question + " Character is:" + character);
			System.out.println("Answer" + this.name + " player question: " + question + " Character is:" + character);

			answer = reader.readLine();

			System.out.println("Player " + this.name + ": " + answer);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer;
	}

	@Override
	public boolean isReadyForGuess() {
		String answer = "";

		try {
			this.writer.println("Are you ready to guess? ");
			System.out.println("Are you ready to guess? ");

			answer = reader.readLine();

			System.out.println("Player " + this.name + ": " + answer);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer.equals("Yes") ? true : false;
	}

	@Override
	public String answerGuess(String guess, String character) {
		String answer = "";

		try {

			this.writer.println("Write your answer: ");
			System.out.println("Write your answer: ");

			answer = reader.readLine();

			System.out.println("Player " + this.name + ": " + answer);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}
=======
    private String askGuess() {
	String answer = "";

	try {
	    writer.println("Write your guess: ");
	    answer = reader.readLine();
	} catch (IOException e) {

	    e.printStackTrace();
	}
	return answer;
    }
    
    private String askName() {
	try {
	    writer.println("Please, name yourself.");
	    this.name = reader.readLine();
	    if (this.name.equals("")) {
		this.name = "Player " + Math.random() * 10;
	    }
	    writer.println("Your name: " + this.name);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return this.name;
    }
    
    private String askQueston() {
	String question = "";

	try {
	    writer.println("Ask your questinon: ");
	    question = reader.readLine();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return question;
    }
    
    private String doAnswerQuestion() {
	String answer = "";
	try {
	    writer.println("Answer second player question: " + this.question + "Character is:" + this.character);
	    answer = reader.readLine();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return answer;
    }
    
    private String doAnswerGuess() {
	String answer = "";

	try {
	    writer.println("Write your answer: ");
	    answer = reader.readLine();
	} catch (IOException e) {
>>>>>>> Stashed changes

	    e.printStackTrace();
	}
	return answer;
    }
}
