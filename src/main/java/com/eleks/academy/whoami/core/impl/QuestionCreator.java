package com.eleks.academy.whoami.core.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eleks.academy.whoami.core.QuestionsBase;

public class QuestionCreator implements QuestionsBase{
	private Map <String, List <String>> questions = new HashMap<>();

	public QuestionCreator() {
		questions.put("creature", new ArrayList<>(creature));
		questions.put("reality", new ArrayList<>(reality));
		questions.put("gender", new ArrayList<>(gender));
		questions.put("age", new ArrayList<>(age));
		questions.put("hairColor", new ArrayList<>(hairColor));
	}
	
	public Map <String, List <String>> getQuestions() {
		return new HashMap<>(questions);
	}
	
}
