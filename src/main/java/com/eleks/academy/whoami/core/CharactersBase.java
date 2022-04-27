package com.eleks.academy.whoami.core;

public interface CharactersBase {

	enum Batman{
		NAME("Batman"), CREATURE("human"), REALITY("fiction"), GENDER("male"), AGE("adult"), HAIRCOLOR("black");
		String str;

		Batman(String str) {
			this.str = str;
		}

		public String getStr() {
			return this.str;
		}
	}

	enum Arestowych{
		NAME("Arestowych"), CREATURE("human"), REALITY("real"), GENDER("male"), AGE("adult"), HAIRCOLOR("black");
		String str;

		Arestowych(String str) {
			this.str = str;
		}

		public String getStr() {
			return this.str;
		}
	}

	enum ScoobyDoo{
		NAME("Scooby Doo"), CREATURE("dog"), REALITY("fiction"), GENDER("male"), AGE("adult"), HAIRCOLOR("brown");
		String str;

		ScoobyDoo(String str) {
			this.str = str;
		}

		public String getStr() {
			return this.str;
		}
	}

	enum Garfield{
		NAME("Garfield"), CREATURE("cat"), REALITY("fiction"), GENDER("male"), AGE("adult"), HAIRCOLOR("red");
		String str;

		Garfield(String str) {
			this.str = str;
		}

		public String getStr() {
			return this.str;
		}
	}
}
