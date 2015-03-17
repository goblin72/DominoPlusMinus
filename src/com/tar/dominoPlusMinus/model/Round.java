package com.tar.dominoPlusMinus.model;

import com.tar.dominoPlusMinus.utils.Utils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * User: goblin72
 * Date: 10.02.2015
 * Time: 11:17
 */
public class Round implements Serializable {
    private Sign sign;
    private Integer[] scoreDelta;
    private ListOfModifiers[] modifiers;
    private Integer[] spentScore;
    private Integer[] earnedScore;

    public Round(int numOfPlayers) {
        scoreDelta = new Integer[numOfPlayers];
        Utils.initValaues(scoreDelta);
        spentScore = new Integer[numOfPlayers];
        Utils.initValaues(spentScore);
        earnedScore = new Integer[numOfPlayers];
        Utils.initValaues(earnedScore);
        modifiers = new ListOfModifiers[numOfPlayers];
        for (int i=0;i<numOfPlayers;i++)
        {
            modifiers[i]=new ListOfModifiers();
        }

    }

    public Integer[] getEarnedScore() {
        return earnedScore;
    }

    public void setEarnedScore(Integer[] earnedScore) {
        this.earnedScore = earnedScore;
    }

    public Integer[] getSpentScore() {
        return spentScore;
    }

    public void setSpentScore(Integer[] spentScore) {
        this.spentScore = spentScore;
    }

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public Integer[] getScoreDelta() {
        return scoreDelta;
    }

    public void setScoreDelta(Integer[] scoreDelta) {
        this.scoreDelta = scoreDelta;
    }

    public ListOfModifiers[] getModifiers() {
        return modifiers;
    }

    public void setModifiers(ListOfModifiers[] modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public String toString() {
        return "Round{" +
                "sign=" + sign +
                ", scoreDelta=" + Arrays.toString(scoreDelta) +
                ", modifiers=" + Arrays.toString(modifiers) +
                '}';
    }
}
