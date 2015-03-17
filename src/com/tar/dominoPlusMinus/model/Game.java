package com.tar.dominoPlusMinus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * User: goblin72
 * Date: 10.02.2015
 * Time: 11:17
 */
public class Game implements Serializable {

    private Player[] players;
    private List<Round> rounds;
    private Date start;
    private boolean isFinished=false;

    public Player[] getPlayers() {
        return players;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public List<Round> getRounds() {
        if (rounds == null)
            rounds = new ArrayList<Round>();
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "Game{" +
                "players=" + Arrays.toString(players) +
                ", rounds=" + rounds +
                ", start=" + start +
                '}';
    }
}
