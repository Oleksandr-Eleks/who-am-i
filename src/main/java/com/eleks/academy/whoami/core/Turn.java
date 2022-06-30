package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.core.impl.SynchronousPlayer;
import com.eleks.academy.whoami.enums.QuestionAnswer;

import java.util.List;

public interface Turn {

	SynchronousPlayer getGuesser();
	
	List<SynchronousPlayer> getOtherPlayers();

	List<QuestionAnswer> getPlayersAnswers();
	Turn changeTurn();

}
