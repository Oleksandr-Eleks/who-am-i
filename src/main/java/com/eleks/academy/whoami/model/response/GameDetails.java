package com.eleks.academy.whoami.model.response;

import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import com.eleks.academy.whoami.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDetails {

    private String id;
    private GameStatus status;
    private List<PersistentPlayer> players;
    private List<PersistentPlayer> winners;

    public GameDetails(PersistentGame game) {
        this.id = game.getGameId();
        this.status = game.getStatus();
        this.players = game.getPLayers();
        this.winners = game.getWinnerList();
    }

}
