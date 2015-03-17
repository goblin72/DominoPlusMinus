package com.tar.dominoPlusMinus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.tar.dominoPlusMinus.dialogs.RoundScoreDialogBuilder;
import com.tar.dominoPlusMinus.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: goblin72
 * Date: 10.02.2015
 * Time: 11:32
 */
public class GameActivity extends AbstractActivity {

    private static GameActivity instance;

    private final String ACTION_TAG = "GameActivity";
    private Game currentGame;
    private Sign currentSign;
    private List<Integer> currentPlayersPoints;
    private List<Button> playersScore;
   // private AlertDialog.Builder scoreDialog;

    private int cellWidth=300;
    private int textSize=30;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        Log.d(ACTION_TAG, "onCreate");
        init(getIntent());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(ACTION_TAG, "onStop "+currentPlayersPoints);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(ACTION_TAG, "onDestroy "+currentPlayersPoints);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(ACTION_TAG, "onRestart "+currentPlayersPoints);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(ACTION_TAG, "onResume "+currentPlayersPoints);
       // init(getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(ACTION_TAG, "onActivityResult "+data+" " +currentPlayersPoints);
        if (data == null) {return;}
        init(data);
        saveGame(null);
    }

    public static GameActivity getInstance() {
        return instance;
    }

    private void init(Intent intent) {
        instance=this;
        RoundScoreDialogBuilder.setContext(this);

        GridLayout roundsGrid = (GridLayout) findViewById(R.id.rounds);
        roundsGrid.post(new Runnable() {
            @Override
            public void run() {
                ScrollView sv = (ScrollView)findViewById(R.id.scrolledRounds);
                Log.d(ACTION_TAG, "scroll2 "+sv.getBottom());
                sv.scrollTo(0, sv.getBottom());
            }
        });

        Game game = (Game) intent.getSerializableExtra(Constants.PARAM_GAME);
        if (game != null) {
            loadGame(game);
            intent.removeExtra(Constants.PARAM_GAME);
        }
        Round round = (Round) intent.getSerializableExtra(Constants.PARAM_ROUND);
        if (round != null) {
            intent.removeExtra(Constants.PARAM_ROUND);
            currentPlayersPoints = addToGrid(roundsGrid, currentPlayersPoints, round);
            currentGame.getRounds().add(round);

            updateScore(round);
            if (round.getSign()!=Sign.ZERO)
                checkWinner();
        }
        //initScoreDialog();
    }

    private void updateScore(Round round) {
        for (int i = 0; i < currentPlayersPoints.size(); i++) {
            int currentScoreAbs = Math.abs(currentPlayersPoints.get(i));
            if (currentScoreAbs == 0 && round.getScoreDelta()[i]!=0) {
                updateAndRedrawPlayersScore(i, 4);
            } else if (currentScoreAbs < 6 && (round.getScoreDelta()[i]!=0 || round.getModifiers()[i].getModifiers().contains(Modifiers.PALINDROME))) {
                updateAndRedrawPlayersScore(i, 1);
            }
        }
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public Sign getCurrentSign() {
        return currentSign;
    }

    public void updateAndRedrawPlayersScore(int playerIndex, int scoreDelta)
    {
        Round lastRound=currentGame.getRounds().get(currentGame.getRounds().size()-1);
        if (scoreDelta>0)
            lastRound.getEarnedScore()[playerIndex]=lastRound.getEarnedScore()[playerIndex]+scoreDelta;
        else
            lastRound.getSpentScore()[playerIndex]=lastRound.getSpentScore()[playerIndex]-scoreDelta;

        currentGame.getPlayers()[playerIndex].updateScore(scoreDelta);
        playersScore.get(playerIndex).setText(getStringScore(currentGame.getPlayers()[playerIndex].getScore()));
    }

    private void checkWinner() {

        List<Integer> uniqueScores = new ArrayList<Integer>();

        for (int i = 0; i < currentPlayersPoints.size(); i++) {
            int currentUserScoreAbs = Math.abs(currentPlayersPoints.get(i));
            if (uniqueScores.contains(currentUserScoreAbs)) {
                //we have winner by equal points!!!
                showWinner(null);
            } else {
                uniqueScores.add(currentUserScoreAbs);
            }

            if (currentUserScoreAbs >= 500) {
                //we have winner by over limit points!!!
                showWinner(i);
            }
            if (currentGame.getPlayers()[i].getScore() >= 10) {
                //we have winner by score!!!
                showWinner(null);
            }
        }
    }

    private void showWinner(Integer exceptUserNumber) {
        currentGame.setFinished(true);
        saveGame(null);
        int maxScore = 0;
        List<Integer> winners = new ArrayList<Integer>();

        for (int i = 0; i < currentPlayersPoints.size(); i++) {
            if (exceptUserNumber != null && i == exceptUserNumber)
                continue;
            int currentScore = currentGame.getPlayers()[i].getScore();
            if (currentScore > maxScore) {
                maxScore = currentScore;
                winners.clear();
                winners.add(i);
            } else if (currentScore == maxScore) {
                winners.add(i);
            }
        }

        Statistics stat = new Statistics(currentGame, winners);
        Intent intent = new Intent(this, GameResultActivity.class);
        intent.putExtra(Constants.PARAM_STAT, stat);
        startActivity(intent);
        finish();
    }

    private void loadGame(final Game game) {
        Log.d(ACTION_TAG, "load game " + game);
        currentGame = game;
        Log.d(ACTION_TAG, currentGame.toString());
        if (currentGame.getRounds() == null || currentGame.getRounds().size() == 0) {
            setSign(Sign.ZERO);
        } else {
            setSign(currentGame.getRounds().get(currentGame.getRounds().size() - 1).getSign());
        }

        GridLayout roundsHeaderGrid = (GridLayout) findViewById(R.id.rounds_header);
        roundsHeaderGrid.setColumnCount(currentGame.getPlayers().length);
        roundsHeaderGrid.removeAllViewsInLayout();
        for (Player player : currentGame.getPlayers()) {
            TextView newEditText = new TextView(getApplicationContext());
            newEditText.setMinWidth(cellWidth);
            newEditText.setMaxWidth(cellWidth);
            newEditText.setText(player.getName());
            newEditText.setTextSize(textSize);
            newEditText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            roundsHeaderGrid.addView(newEditText);
        }
        currentPlayersPoints = new ArrayList<Integer>();
        playersScore=new ArrayList<Button>();
        int i=0;
        for (Player player : currentGame.getPlayers()) {
            Button newEditText = new Button(getApplicationContext());
            newEditText.setMinWidth(cellWidth);
            newEditText.setMaxWidth(cellWidth);
            newEditText.setTextSize(textSize);
            newEditText.setGravity(Gravity.CENTER);
            switch (i)
            {
                case 0:{
                    newEditText.setId(R.id.playerName1);
                    break;
                }
                case 1:{
                    newEditText.setId(R.id.playerName2);
                    break;
                }
                case 2:{
                    newEditText.setId(R.id.playerName3);
                    break;
                }
                case 4:{
                    newEditText.setId(R.id.playerName4);
                    break;
                }
            }
          //  final AlertDialog.Builder scoreDialog =RoundScoreDialogBuilder.getInstance(this);

            newEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickerIndex=-1;
                    switch (view.getId())
                    {
                        case R.id.playerName1:{
                            clickerIndex=0;
                            break;
                        }
                        case R.id.playerName2:{
                            clickerIndex=1;
                            break;
                        }
                        case R.id.playerName3:{
                            clickerIndex=2;
                            break;
                        }
                        case R.id.playerName4:{
                            clickerIndex=3;
                            break;
                        }
                    }
                    Log.d(ACTION_TAG, "click "+clickerIndex);
                    Player clicker = game.getPlayers()[clickerIndex];
                    if (clicker.getScore()==0) {
                        showToast(getString(R.string.no_score));
                        return;
                    }
                    AlertDialog.Builder scoreDialog =new RoundScoreDialogBuilder(clickerIndex);

                    scoreDialog.show();
                }
            });

            i++;
            newEditText.setText(getStringScore(player.getScore()));
            roundsHeaderGrid.addView(newEditText);
            playersScore.add(newEditText);
            currentPlayersPoints.add(0);
        }


        GridLayout roundsGrid = (GridLayout) findViewById(R.id.rounds);
        roundsGrid.setColumnCount(currentGame.getPlayers().length + 1);

        if (currentGame.getRounds() != null) {
            for (Round round : currentGame.getRounds()) {
                currentPlayersPoints = addToGrid(roundsGrid, currentPlayersPoints, round);
            }
        }
    }

    public void saveGame(View view) {
        Log.d(ACTION_TAG, "saveGame start!");
        String fileName = String.valueOf(currentGame.getStart().getTime());
        ObjectOutputStream oos = null;
        try {
            FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(outputStream);
            oos.writeObject(currentGame);
            oos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (oos!=null)
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        for (File f:getFilesDir().listFiles())
        {
            Log.d(ACTION_TAG, "list files: "+f.getName());
        }
        Log.d(ACTION_TAG, "saveGame finish!");
       // showToast(getString(R.string.game_saved));
    }

    public void endRound(View view) {
        if (currentGame.isFinished())
        {
            checkWinner();
            return;
        }
        Intent intent = new Intent(this, RoundResultsActivity.class);
        intent.putExtra(Constants.PARAM_PLAYERS, new ListOfPlayers(currentGame.getPlayers()));
        intent.putExtra(Constants.PARAM_PLAYERS_SCORE, new ListOfInteger(currentPlayersPoints));
        intent.putExtra(Constants.PARAM_SIGN, currentSign);
        startActivityForResult(intent, 1);
    }

    public void changeSign(View view) {
        if (currentGame.isFinished())
        {
            showToast(getString(R.string.game_over));
            return;
        }

        if (currentSign == Sign.PLUS)
            setSign(Sign.MINUS);
        else if (currentSign == Sign.MINUS)
            setSign(Sign.PLUS);
        else {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.select_sign)).setPositiveButton(getString(R.string.plus), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    setSign(Sign.PLUS);
                    dialog.cancel();
                }
            }).setNegativeButton(getString(R.string.minus), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    setSign(Sign.MINUS);
                    dialog.cancel();
                }
            })
                    .show();

        }
    }

    public void menu(View view) {
        Intent intent = new Intent(this, DominoPlusMinus.class);
        startActivity(intent);
        finish();
    }



    public void setSign(Sign sign) {
        currentSign = sign;
        Button changeSign = (Button) findViewById(R.id.change_sign);
        switch (sign) {
            case MINUS: {
                changeSign.setText(getString(R.string.minus));
                changeSign.setBackgroundColor(getResources().getColor(R.color.red));
                break;
            }
            case PLUS: {
                changeSign.setText(getString(R.string.plus));
                changeSign.setBackgroundColor(getResources().getColor(R.color.green));
                break;
            }
            case ZERO: {
                changeSign.setText(getString(R.string.zero));
                changeSign.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            }
        }
    }

    public List<Integer> addToGrid(GridLayout roundsGrid, List<Integer> currentPlayersPoints, Round round) {
        Log.d(ACTION_TAG, "add round " + round);
        String sign = getString(R.string.zero);
        for (int i = 0; i < currentPlayersPoints.size(); i++) {
            int newScore = currentPlayersPoints.get(i);
            newScore += round.getScoreDelta()[i];
            switch (round.getSign()) {
                case PLUS: {
                    sign = getString(R.string.plus);
                    break;
                }
                case MINUS: {
                    sign = getString(R.string.minus);
                    break;
                }
            }

            ListOfModifiers currentModifierList = round.getModifiers()[i];
            String modifierSign = "";
            boolean goFirst=false;
            boolean fakeRound=false;
            for (Modifiers m : currentModifierList.getModifiers()) {
                switch (m) {
                    case PALINDROME: {
                        modifierSign = " " + getString(R.string.palindrome_sign);
                        break;
                    }
                    case ANTI_PALINDROME: {
                        modifierSign = " " + getString(R.string.anti_palindrome_sign);
                        break;
                    }
                    case FISH: {
                        modifierSign = " " + getString(R.string.fish_sign);
                        goFirst=true;
                        break;
                    }
                    case RESULT_OF_SCORE_OPTION: {
                        fakeRound=true;
                        break;
                    }
                }
            }
            goFirst=goFirst||round.getScoreDelta()[i] == 0;

            currentPlayersPoints.set(i, newScore);
            TextView newEditText = new TextView(getApplicationContext());
            newEditText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            newEditText.setMinWidth(cellWidth);
            newEditText.setMaxWidth(cellWidth);
            newEditText.setPadding(0,0,0,5);
            newEditText.setTextSize(textSize);
            if (goFirst)
                newEditText.setTextColor(getResources().getColor(R.color.green));
            if (fakeRound)
                newEditText.setTextColor(getResources().getColor(R.color.yellow));

            newEditText.setText(round.getScoreDelta()[i] == 0 ? ("-" + modifierSign) : (newScore + modifierSign));
            roundsGrid.addView(newEditText);
        }

        TextView newEditText = new TextView(getApplicationContext());
        newEditText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        newEditText.setMinWidth(30);
        newEditText.setMaxWidth(30);
        newEditText.setText(sign);
        newEditText.setTextSize(textSize);
        roundsGrid.addView(newEditText);

        return currentPlayersPoints;
    }

    public void updateLastRound(int subjectIndex, int scoreDelta) {
        GridLayout roundsGrid = (GridLayout) findViewById(R.id.rounds);
        Round round = new Round(currentPlayersPoints.size());

        if (scoreDelta<0)
            round.setSign(Sign.MINUS);
        else
            round.setSign(Sign.PLUS);

        for (int i=0; i<currentPlayersPoints.size();i++)
        {
            if (i!=subjectIndex) {
                round.getScoreDelta()[i] = 0;
            }
            else
                round.getScoreDelta()[i] = Math.abs(scoreDelta);
            round.getModifiers()[i].getModifiers().add(Modifiers.RESULT_OF_SCORE_OPTION);
        }

        currentGame.getRounds().add(round);

        currentPlayersPoints=addToGrid(roundsGrid, currentPlayersPoints, round);
    }
}
