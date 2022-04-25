package com.eleks.academy.whoami.core.card;

import java.util.List;

public interface Card {
    void addCards(List<String> cards);

    void addNewCard(String card);

    List<String> getCards();

    void removeCards();
}
