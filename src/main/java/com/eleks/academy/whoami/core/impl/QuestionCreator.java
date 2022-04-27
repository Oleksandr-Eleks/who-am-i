package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.QuestionsBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionCreator {
	private Map<String, List<String>> questions = new HashMap<>();

	public QuestionCreator() {
		questions.put("creature", QuestionsBase.CREATURE.getCharacteristics());
		questions.put("reality", QuestionsBase.REALITY.getCharacteristics());
		questions.put("gender", QuestionsBase.GENDER.getCharacteristics());
		questions.put("age", QuestionsBase.AGE.getCharacteristics());
		questions.put("hairColor", QuestionsBase.HAIRCOLOR.getCharacteristics());
	}
	
	public Map <String, List <String>> getQuestions() {
		return new HashMap<>(questions);
	}
}
