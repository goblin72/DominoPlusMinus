package com.tar.dominoPlusMinus.scoreOptions;

import com.tar.dominoPlusMinus.GameActivity;
import com.tar.dominoPlusMinus.R;
import com.tar.dominoPlusMinus.model.Sign;

/**
 * User: goblin72
 * Date: 12.02.2015
 * Time: 12:43
 */
public enum RoundScoreOptions
{
    PLUS_POINT(2, R.string.plus_point, true, null),
    MINUS_POINT(2, R.string.minus_point, true, null),
    MISS_TURN(1, R.string.miss_turn, false, null),
    TURN_SIGN_ZERO1(1, R.string.turn_sign_zero, false, Sign.PLUS),
    TURN_SIGN_ZERO2(1, R.string.turn_sign_zero, false, Sign.MINUS),
    TURN_SIGN_PLUS(1, R.string.turn_sign_plus, false, Sign.ZERO),
    TURN_SIGN_MINUS(1, R.string.turn_sign_minus, false, Sign.ZERO),
    REVERT_SIGN1(2, R.string.revert_sign, false, Sign.PLUS),
    REVERT_SIGN2(2, R.string.revert_sign, false, Sign.MINUS);

    public int costs;
    public int nameId;
    public boolean needSubject;
    public Sign condition;

    RoundScoreOptions(int costs, int nameId, boolean needSubject, Sign condition) {
        this.costs = costs;
        this.nameId = nameId;
        this.needSubject = needSubject;
        this.condition = condition;
    }

    public void executeScoreOption(int clickerIndex, int subjectIndex)
    {
        GameActivity gameActivity =GameActivity.getInstance();
        gameActivity.updateAndRedrawPlayersScore(clickerIndex, -costs);


        switch (this)
        {
            case REVERT_SIGN1:{}
            case REVERT_SIGN2:{gameActivity.changeSign(null); break;}

            case TURN_SIGN_ZERO1:{}
            case TURN_SIGN_ZERO2:{gameActivity.setSign(Sign.ZERO); break;}

            case PLUS_POINT: {gameActivity.updateLastRound(subjectIndex, 1);break;}
            case MINUS_POINT:{gameActivity.updateLastRound(subjectIndex, -1);break;}

            case TURN_SIGN_MINUS:{gameActivity.setSign(Sign.MINUS); break;}
            case TURN_SIGN_PLUS:{gameActivity.setSign(Sign.PLUS); break;}
        }
    }
}
