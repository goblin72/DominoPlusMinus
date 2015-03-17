package com.tar.dominoPlusMinus;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.tar.dominoPlusMinus.dialogs.AfterRoundScoreDialogBuilder;
import com.tar.dominoPlusMinus.dialogs.RoundScoreDialogBuilder;
import com.tar.dominoPlusMinus.model.*;

/**
 * User: goblin72
 * Date: 10.02.2015
 * Time: 18:04
 */
public class RoundResultsActivity extends AbstractActivity {
    private final String ACTION_TAG = "RoundResultsAction";

    Player[] players;
    Integer[] points;
    Sign sign;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        Log.d(ACTION_TAG, "onCreate");
        init(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(ACTION_TAG, "onResume");
        init(getIntent());
    }

    private void init( Intent intent) {
       if (intent.getSerializableExtra(Constants.PARAM_PLAYERS)==null) {
           Log.w(ACTION_TAG, "no input params!");
           return;
       }

        players = ((ListOfPlayers) intent.getSerializableExtra(Constants.PARAM_PLAYERS)).getPlayers();
        points = ((ListOfInteger) intent.getSerializableExtra(Constants.PARAM_PLAYERS_SCORE)).getIntegers();
        sign = (Sign) intent.getSerializableExtra(Constants.PARAM_SIGN);

        ((TextView) findViewById(R.id.playerName1)).setText(players[0].getName());
        ((TextView) findViewById(R.id.playerName2)).setText(players[1].getName());
        switch (players.length) {
            case 2: {
                findViewById(R.id.playerName3).setVisibility(View.INVISIBLE);
                findViewById(R.id.playerFish3).setVisibility(View.INVISIBLE);
                findViewById(R.id.playerScore3).setVisibility(View.INVISIBLE);
                findViewById(R.id.playerName4).setVisibility(View.INVISIBLE);
                findViewById(R.id.playerFish4).setVisibility(View.INVISIBLE);
                findViewById(R.id.playerScore4).setVisibility(View.INVISIBLE);

                break;
            }
            case 3: {
                findViewById(R.id.playerName3).setVisibility(View.VISIBLE);
                findViewById(R.id.playerFish3).setVisibility(View.VISIBLE);
                findViewById(R.id.playerScore3).setVisibility(View.VISIBLE);
                findViewById(R.id.playerName4).setVisibility(View.INVISIBLE);
                findViewById(R.id.playerFish4).setVisibility(View.INVISIBLE);
                findViewById(R.id.playerScore4).setVisibility(View.INVISIBLE);

                ((TextView) findViewById(R.id.playerName3)).setText(players[2].getName());
                break;
            }
            case 4: {
                findViewById(R.id.playerName3).setVisibility(View.VISIBLE);
                findViewById(R.id.playerFish3).setVisibility(View.VISIBLE);
                findViewById(R.id.playerScore3).setVisibility(View.VISIBLE);
                findViewById(R.id.playerName4).setVisibility(View.VISIBLE);
                findViewById(R.id.playerFish4).setVisibility(View.VISIBLE);
                findViewById(R.id.playerScore4).setVisibility(View.VISIBLE);

                ((TextView) findViewById(R.id.playerName3)).setText(players[2].getName());
                ((TextView) findViewById(R.id.playerName4)).setText(players[3].getName());
                break;
            }
        }

        ((EditText) findViewById(R.id.playerScore1)).setText("0");
        ((EditText) findViewById(R.id.playerScore2)).setText("0");
        ((EditText) findViewById(R.id.playerScore3)).setText("0");
        ((EditText) findViewById(R.id.playerScore4)).setText("0");

        ((CheckBox) findViewById(R.id.playerFish1)).setChecked(false);
        ((CheckBox) findViewById(R.id.playerFish2)).setChecked(false);
        ((CheckBox) findViewById(R.id.playerFish3)).setChecked(false);
        ((CheckBox) findViewById(R.id.playerFish4)).setChecked(false);
    }

    public void cancel(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    private Round tempRound;

    public Round prepareRoundObject()
    {
        if (tempRound==null) {


            Round round = new Round(players.length);
            round.setSign(sign);

            for (int i = 0; i < players.length; i++) {
                if (isFish(i)) {
                    round.getModifiers()[i].getModifiers().add(Modifiers.FISH);
                }
                int tempRes = 0;
                switch (sign) {

                    case ZERO: {
                        break;
                    }
                    case PLUS: {
                        tempRes = points[i] + getScore(i);
                        break;
                    }
                    case MINUS: {
                        tempRes = points[i] - getScore(i);
                        break;
                    }
                }
                boolean addPalindromeModifier = false;
                while (tempRes != 0 && ifPalindrome(tempRes)) {
                    int tempRes2 = decreaseFirstSign(tempRes);
                    if (!ifAntiPalindrome(tempRes2)) {
                        tempRes = tempRes2;
                        round.getScoreDelta()[i] = tempRes2;
                        addPalindromeModifier = true;
                    } else
                    {
                        break;
                    }
                }
                if (addPalindromeModifier)
                    round.getModifiers()[i].getModifiers().add(Modifiers.PALINDROME);

                boolean addAntiPalindromeModifier = false;
                while (tempRes != 0 && ifAntiPalindrome(tempRes)) {
                    int tempRes2 = increaseFirstSign(tempRes);
                    if (!ifPalindrome(tempRes2)) {
                        tempRes = tempRes2;
                        addAntiPalindromeModifier = true;
                    }   else
                    {
                        break;
                    }
                }
                if (addAntiPalindromeModifier)
                    round.getModifiers()[i].getModifiers().add(Modifiers.ANTI_PALINDROME);

                if (sign.equals(Sign.ZERO))
                    round.getScoreDelta()[i]=0;
                else
                    round.getScoreDelta()[i] = tempRes - points[i];
            }
            tempRound=round;
        }
        return tempRound;
    }



    public void save(View view) {
        if (!validateInput())
        {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.PARAM_ROUND, prepareRoundObject());
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean validateInput() {
        boolean hasZero=false;
        boolean hasFish=false;
        for (int i =0; i<players.length; i++)
        {
            int score = getScore(i);
            if (score==0 && hasZero) {
                showToast(getString(R.string.points_validation1));
                return false;
            } else
            {
                hasZero=hasZero||score==0;
            }

            boolean fish = isFish(i);
            if (fish && hasFish) {
                showToast(getString(R.string.points_validation2));
                return false;
            } else
            {
                hasFish=hasFish||fish;
            }
        }

        if (!hasZero && !hasFish)
        {
            showToast(getString(R.string.points_validation3));
            return false;
        } else if (hasZero && hasFish)
        {
            showToast(getString(R.string.points_validation4));
            return false;
        }
        return true;
    }

    private int increaseFirstSign(int tempRes) {
        String strAbsNum = String.valueOf(Math.abs(tempRes));
        int firstSign = Integer.parseInt(String.valueOf(strAbsNum).substring(0, 1));
        String restOfNum = String.valueOf(strAbsNum).substring(1, strAbsNum.length());
        int resultAbs = Integer.parseInt((firstSign + 1) + restOfNum);
        return tempRes > 0 ? resultAbs : -resultAbs;
    }

    private boolean ifAntiPalindrome(int tempRes2) {
        int a = Math.abs(tempRes2);
        if (a<48) {
            return false;
        }
        int sum = 0;
        while (a > 0) {
            sum += a % 10;
            a = a / 10;
        }
        return sum == 13;
    }

    private int decreaseFirstSign(int tempRes) {
        String strAbsNum = String.valueOf(Math.abs(tempRes));
        int firstSign = Integer.parseInt(String.valueOf(strAbsNum).substring(0, 1));
        String restOfNum = String.valueOf(strAbsNum).substring(1, strAbsNum.length());
        int resultAbs = Integer.parseInt((firstSign - 1) + restOfNum);
        return tempRes > 0 ? resultAbs : -resultAbs;
    }

    private boolean ifPalindrome(int tempRes) {
        int a = Math.abs(tempRes);
        if (a<11) {
            return false;
        }
        StringBuilder sb = new StringBuilder(String.valueOf(a));
        return sb.toString().equals(sb.reverse().toString());
    }

    private Integer getScore(int userNum) {
        switch (userNum) {
            case 0: {
                return Integer.parseInt(((EditText) findViewById(R.id.playerScore1)).getText().toString());
            }
            case 1: {
                return Integer.parseInt(((EditText) findViewById(R.id.playerScore2)).getText().toString());
            }
            case 2: {
                return Integer.parseInt(((EditText) findViewById(R.id.playerScore3)).getText().toString());
            }
            case 3: {
                return Integer.parseInt(((EditText) findViewById(R.id.playerScore4)).getText().toString());
            }
        }
        return 0;
    }

    private boolean isFish(int userNum) {
        switch (userNum) {
            case 0: {
                return ((CheckBox) findViewById(R.id.playerFish1)).isChecked();
            }
            case 1: {
                return ((CheckBox) findViewById(R.id.playerFish2)).isChecked();
            }
            case 2: {
                return ((CheckBox) findViewById(R.id.playerFish3)).isChecked();
            }
            case 3: {
                return ((CheckBox) findViewById(R.id.playerFish4)).isChecked();
            }
        }
        return false;
    }

    public void useBonuses(View view)
    {
        AlertDialog.Builder scoreDialog =new AfterRoundScoreDialogBuilder(this);

        scoreDialog.show();
    }

}
