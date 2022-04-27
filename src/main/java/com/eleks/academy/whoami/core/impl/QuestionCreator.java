package com.eleks.academy.whoami.core.impl;
import java.util.*;

import com.eleks.academy.whoami.core.QuestionsBase;

public class QuestionCreator implements QuestionsBase{
	private Map <String, List <String>> questions = new HashMap<>();

	public QuestionCreator() {
		questions.put("creature", List.of(Creature.HUMAN.getStr(), Creature.CAT.getStr(),Creature.DOG.getStr()));
		questions.put("reality", List.of(Reality.REAL.getStr(), Reality.FICTION.getStr()));
		questions.put("gender", List.of(Gender.MALE.getStr(), Gender.FEMALE.getStr()));
		questions.put("age", List.of(Age.ADULT.getStr()));
		questions.put("hairColor", List.of(HairColor.RED.getStr(), HairColor.BLACK.getStr(), HairColor.BROWN.getStr()));
	}
	
	public Map <String, List <String>> getQuestions() {
		return new HashMap<>(questions);
	}
}
