package com.eleks.academy.whoami.core.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eleks.academy.whoami.core.QuestionsBase;

public class QuestionCreator implements QuestionsBase{
	private Map <String, List <String>> questions = new HashMap<>();

	public QuestionCreator() {
		questions.put("creature", questionCreate(creature));
		questions.put("reality", questionCreate(reality));
		questions.put("gender", questionCreate(gender));
		questions.put("age", questionCreate(age));
		questions.put("hairColor", questionCreate(hairColor));
	}

	public List<String> questionCreate(List<String> fromBase) {	
		List <String> temp = new ArrayList<>();
		for (int i = 0; i < fromBase.size(); i++) {
			temp.add(fromBase.get(i));
		}
		return temp;
	}	
	
	public Map <String, List <String>> getQuestions() {
		return new HashMap<>(questions);
	}
	
}
