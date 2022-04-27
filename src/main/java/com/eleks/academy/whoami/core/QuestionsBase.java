package com.eleks.academy.whoami.core;

public interface QuestionsBase {

	enum Creature{
		HUMAN("human"), CAT("cat"), DOG("dog");
		private String str;

		Creature (String str){
			this.str = str;
		}

		public String getStr(){
			return this.str;
		}
	}

	enum Reality{
		REAL("real"), FICTION("fiction");
		private String str;

		Reality (String str){
			this.str = str;
		}

		public String getStr(){
			return this.str;
		}
	}

	enum Gender{
		MALE("male"), FEMALE("female");
		private String str;

		Gender(String str) {
			this.str = str;
		}

		public String getStr() {
			return this.str;
		}
	}

	enum Age{
		ADULT ("adult");
		private String str;

		Age(String str) {
			this.str = str;
		}

		public String getStr() {
			return this.str;
		}
	}

	enum HairColor{
		RED("red"), BLACK("black"), BROWN("brown");
		private String str;

		HairColor(String str) {
			this.str = str;
		}

		public String getStr() {
			return this.str;
		}
	}
}
