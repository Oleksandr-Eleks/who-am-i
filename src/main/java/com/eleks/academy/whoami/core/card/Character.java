package com.eleks.academy.whoami.core.card;

import java.util.List;

public class Character implements Card {
    private List<String> characters;

    @Override
    public void addCards(List<String> cards) {
        this.characters = cards;
    }

    @Override
    public void addNewCard(String card) {
        characters.add(card);
    }

    @Override
    public List<String> getCards() {
        return this.characters;
    }

    @Override
    public void removeCards() {
        characters.clear();
    }
}
