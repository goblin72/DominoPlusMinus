package com.tar.dominoPlusMinus.model;

import android.util.Log;
import com.tar.dominoPlusMinus.utils.Utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: goblin72
 * Date: 11.02.2015
 * Time: 12:27
 */
public class Statistics implements Serializable {

    int rounds;
    String[] players;
    Integer[] points;
    Integer[] score;
    Integer[] scoreSpent;
    Integer[] scoreEarned;
    Integer[] wins;
    Integer[] fishes;
    Integer[] palindromes;
    Integer[] antiPalindromes;
    Integer[] maxDelta;
    Integer[] maxPoints;
    StringBuilder winner = new StringBuilder();
    Date start;

    public Statistics(Game game, List<Integer> winnerIndex) {
        int numberOfPlayers = game.getPlayers().length;
        players = new String[numberOfPlayers];

        wins = new Integer[numberOfPlayers];
        Utils.initValaues(wins);
        fishes = new Integer[numberOfPlayers];
        Utils.initValaues(fishes);
        palindromes = new Integer[numberOfPlayers];
        Utils.initValaues(palindromes);
        antiPalindromes = new Integer[numberOfPlayers];
        Utils.initValaues(antiPalindromes);
        maxDelta = new Integer[numberOfPlayers];
        Utils.initValaues(maxDelta);
        maxPoints = new Integer[numberOfPlayers];
        Utils.initValaues(maxPoints);
        points = new Integer[numberOfPlayers];
        Utils.initValaues(points);
        score = new Integer[numberOfPlayers];
        Utils.initValaues(score);
        scoreSpent = new Integer[numberOfPlayers];
        Utils.initValaues(scoreSpent);
        scoreEarned = new Integer[numberOfPlayers];
        Utils.initValaues(scoreEarned);

        this.start = game.getStart();
        for (Integer i : winnerIndex) {
            if (winner.length() > 0) {
                winner.append(", ");
            }
            winner.append(game.getPlayers()[i].getName());
        }
        int i=0;
        for (Player player: game.getPlayers())
        {
            players[i]=player.getName();
            score[i]=player.getScore();
            i++;
        }

        for (Round round:game.getRounds())
        {
            if (!round.getModifiers()[0].getModifiers().contains(Modifiers.RESULT_OF_SCORE_OPTION) )
            {
                 rounds++;
            }

            for (i=0;i<numberOfPlayers;i++)
            {
                scoreSpent[i]=scoreSpent[i]+round.getSpentScore()[i];
                scoreEarned[i]=scoreEarned[i]+round.getEarnedScore()[i];
                if (round.getModifiers()[i].getModifiers().contains(Modifiers.FISH) )
                {
                    wins[i]=wins[i]+1;
                    fishes[i]=fishes[i]+1;
                } else if (round.getScoreDelta()[i]==0)
                {
                    wins[i]=wins[i]+1;
                }

                if (round.getModifiers()[i].getModifiers().contains(Modifiers.PALINDROME) )
                {
                    palindromes[i]=palindromes[i]+1;
                }
                if (round.getModifiers()[i].getModifiers().contains(Modifiers.ANTI_PALINDROME) )
                {
                    antiPalindromes[i]=antiPalindromes[i]+1;
                }

                if (maxDelta[i]<round.getScoreDelta()[i])
                {
                    maxDelta[i]=round.getScoreDelta()[i];
                }


                switch (round.getSign())
                {
                    case PLUS:
                    {
                        points[i]=points[i]+round.getScoreDelta()[i];

                        break;
                    }
                    case MINUS:
                    {
                        points[i]=points[i]+round.getScoreDelta()[i];

                        break;
                    }
                    case ZERO:
                    {

                    }
                }

                if (Math.abs(maxPoints[i])<Math.abs(points[i]))
                    maxPoints[i]=points[i];
            }
        }
    }

    public int getRounds() {
        return rounds;
    }

    public String[] getPlayers() {
        return players;
    }

    public Integer[] getPoints() {
        return points;
    }

    public Integer[] getScore() {
        return score;
    }

    public Integer[] getScoreSpent() {
        return scoreSpent;
    }

    public Integer[] getScoreEarned() {
        return scoreEarned;
    }

    public Integer[] getWins() {
        return wins;
    }

    public Integer[] getFishes() {
        return fishes;
    }

    public Integer[] getPalindromes() {
        return palindromes;
    }

    public Integer[] getAntiPalindromes() {
        return antiPalindromes;
    }

    public Integer[] getMaxDelta() {
        return maxDelta;
    }

    public Integer[] getMaxPoints() {
        return maxPoints;
    }

    public StringBuilder getWinner() {
        return winner;
    }

    public Date getStart() {
        return start;
    }

    @Override
    public String toString() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //todo
        StringBuilder sb=new StringBuilder();
        sb.append("Статистика игры" + "\nИгра начата ").append(format.format(start)).
                append("\nПобедитель ").append(winner.toString()).
                append("\nСыграно раундов ").append(rounds);

        sb.append("\n");
        String line ="\n-------------------------------------------------------------------------";
        sb.append(line);


        addLine(sb, "Игрок", players);
        sb.append(line);
        addLine(sb, "Очков", points);
        addLine(sb, "Бонусов", score);
        addLine(sb, "Получено бон.", scoreEarned);
        addLine(sb, "Потрачено бон.", scoreSpent);
        sb.append(line);
        addLine(sb, "Побед", wins);
        addLine(sb, "Рыб", fishes);
        addLine(sb, "Палиндр.", palindromes);
        addLine(sb, "Антипал.", antiPalindromes);
        sb.append(line);
        addLine(sb, "Макс. очков", maxDelta);
        addLine(sb, "Абсол. макс.", maxPoints);
        sb.append(line);
        Log.d("STAT ", "\n"+sb.toString());
        return sb.toString();
    }

    private void addLine(StringBuilder sb, String header, Integer[] data)
    {
        sb.append("\n| ");
        sb.append(addGaps(header));
        for (Integer cell: data)
        {
            sb.append(" | ").append(addGaps(String.valueOf(cell)));
        }
        sb.append(" | ");
    }

    private void addLine(StringBuilder sb, String header, String[] data)
    {
        sb.append("\n| ");
        sb.append(addGaps(header));
        for (String cell: data)
        {
            sb.append(" | ").append(addGaps(cell));
        }
        sb.append(" | ");
    }

    private int CELL_WIDTH=16;

    private String addGaps(String str)
    {
        StringBuilder sb = new StringBuilder(str);
        while (sb.length()<CELL_WIDTH)
        {
            sb.append(" ");
        }
        return sb.toString();
    }
}
