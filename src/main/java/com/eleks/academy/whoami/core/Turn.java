package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import com.eleks.academy.whoami.enums.QuestionAnswer;

import java.util.List;

public interface Turn {

	PersistentPlayer getCurrentTurn();
	
	List<PersistentPlayer> getOtherPlayers();

	List<QuestionAnswer> getPlayersAnswers();

	Turn changeTurn();

}
