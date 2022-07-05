package com.eleks.academy.whoami.model.response;

import com.eleks.academy.whoami.core.impl.PersistentPlayer;

import java.util.Objects;

public class PlayerDetails {
    private String id;
    private String nickname;

    public PlayerDetails(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public PlayerDetails() {
    }

    public static PlayerDetails of(PersistentPlayer player) {
        return new PlayerDetails(player.getId(), player.getNickname());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerDetails that = (PlayerDetails) o;
        return Objects.equals(id, that.id) && Objects.equals(nickname, that.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickname);
    }

    @Override
    public String toString() {
        return "PlayerDetails{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }

}
