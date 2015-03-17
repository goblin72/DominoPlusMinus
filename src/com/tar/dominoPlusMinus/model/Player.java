package com.tar.dominoPlusMinus.model;

import java.io.Serializable;

/**
 * User: goblin72
 * Date: 10.02.2015
 * Time: 11:21
 */
public class Player implements Serializable {
    private String name;
    private Integer score = 0;

    public void updateScore(int delta) {
        score += delta;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }


}
