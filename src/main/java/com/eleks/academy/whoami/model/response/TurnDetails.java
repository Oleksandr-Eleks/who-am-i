package com.eleks.academy.whoami.model.response;

import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TurnDetails {

    private PersistentPlayer currentPlayer;

    private List<PersistentPlayer> players;

}
