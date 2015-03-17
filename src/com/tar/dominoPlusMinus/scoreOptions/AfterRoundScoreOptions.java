package com.tar.dominoPlusMinus.scoreOptions;

import android.widget.EditText;
import com.tar.dominoPlusMinus.GameActivity;
import com.tar.dominoPlusMinus.R;
import com.tar.dominoPlusMinus.RoundResultsActivity;
import com.tar.dominoPlusMinus.model.Sign;

/**
 * User: goblin72
 * Date: 12.02.2015
 * Time: 12:43
 */
public enum AfterRoundScoreOptions
{
    CHANGE_POINTS(3, R.string.change_points,2),
    REVERT_SIGN(3, R.string.revert_sign,4);
 //   EXORCISM(1, R.string.exorcism,4);

    public int costs;
    public int maxNumberOfPlayers;
    public int nameId;

    AfterRoundScoreOptions(int costs, int nameId, int maxNumberOfPlayers) {
        this.costs = costs;
        this.nameId = nameId;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    public void executeScoreOption(GameActivity game, RoundResultsActivity roundActivity, int clickerIndex)
    {
        game.updateAndRedrawPlayersScore(clickerIndex, -costs);
        switch (this)
        {
            case CHANGE_POINTS: {

                EditText text1 = ((EditText) roundActivity.findViewById(R.id.playerScore1));
                EditText text2 = ((EditText) roundActivity.findViewById(R.id.playerScore2));

                String tmp = text1.getText().toString();

                ((EditText) roundActivity.findViewById(R.id.playerScore1)).setText(text2.getText().toString());
                ((EditText) roundActivity.findViewById(R.id.playerScore2)).setText(tmp);
                break;
            }

            case REVERT_SIGN:{game.changeSign(null);
                Sign sign = roundActivity.getSign();
                if (sign.equals(Sign.PLUS))
                    roundActivity.setSign(Sign.MINUS);
                else if(sign.equals(Sign.MINUS))
                    roundActivity.setSign(Sign.PLUS);
                break;}
        }
    }
}
