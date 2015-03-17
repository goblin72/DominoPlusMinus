package com.tar.dominoPlusMinus.model;

import java.io.Serializable;

/**
 * User: goblin72
 * Date: 11.02.2015
 * Time: 13:25
 */
public class ListOfPlayers implements Serializable {

    Player[] players;

    public ListOfPlayers(Player[] players) {
        this.players = players;
    }

    public Player[] getPlayers() {
        return players;
    }
}
