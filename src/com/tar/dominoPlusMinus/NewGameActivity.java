package com.tar.dominoPlusMinus;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.tar.dominoPlusMinus.model.Game;
import com.tar.dominoPlusMinus.model.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * User: goblin72
 * Date: 09.02.2015
 * Time: 20:41
 */
public class NewGameActivity extends AbstractActivity {
    private final String ACTION_TAG = "NewGameActivity";
    private int numberOfPlayers = 2;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_new_game);
        Log.d(ACTION_TAG, "onCreate");

        File[] files = getFilesDir().listFiles();
        Arrays.sort(files, new FileComparator());
        if (files.length>0)
        {
            try {
                Game lastGame = readGameFromFile(files[0]);
                while (numberOfPlayers<lastGame.getPlayers().length && numberOfPlayers!=4)
                {
                    addPlayer(null);
                }
                int index=0;
                for (Player player: lastGame.getPlayers()) {
                    switch (index) {
                        case 0: {
                            ((EditText) findViewById(R.id.playerName1)).setText(player.getName());
                            break;
                        }
                        case 1: {
                            ((EditText) findViewById(R.id.playerName2)).setText(player.getName());
                            break;
                        }
                        case 2: {
                            ((EditText) findViewById(R.id.playerName3)).setText(player.getName());
                            break;
                        }
                        case 3: {
                            ((EditText) findViewById(R.id.playerName4)).setText(player.getName());
                            break;
                        }
                        default: {
                            throw new RuntimeException("wrong player index");
                        }
                    }
                    index++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void addPlayer(View view) {
        Log.d(ACTION_TAG, "addPlayer");
        switch (numberOfPlayers) {
            case 2: {
                View editText = findViewById(R.id.playerName3);
                editText.setVisibility(View.VISIBLE);
                numberOfPlayers++;
                break;
            }
            case 3: {
                View editText = findViewById(R.id.playerName4);
                editText.setVisibility(View.VISIBLE);
                numberOfPlayers++;
                break;
            }
            default: {
                showToast("Допустимо не более 4 игроков");
            }
        }
    }

    public void deletePlayer(View view) {
        Log.d(ACTION_TAG, "deletePlayer");
        switch (numberOfPlayers) {
            case 3: {
                View editText = findViewById(R.id.playerName3);
                editText.setVisibility(View.INVISIBLE);
                numberOfPlayers--;
                break;
            }
            case 4: {
                View editText = findViewById(R.id.playerName4);
                editText.setVisibility(View.INVISIBLE);
                numberOfPlayers--;
                break;
            }
            default: {
                showToast("Допустимо не менее 2 игроков");
            }
        }
    }

    public void back(View view) {
        Intent intent = new Intent(this, DominoPlusMinus.class);
        startActivity(intent);
        finish();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void start(View view) {
        Log.d(ACTION_TAG, "start");
        Game game = new Game();
        game.setPlayers(new Player[numberOfPlayers]);
        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = new Player();
            player.setScore(0); //todo

            switch (i) {
                case 0: {
                    EditText editText = (EditText) findViewById(R.id.playerName1);
                    player.setName(editText.getText().toString());
                    break;
                }
                case 1: {
                    EditText editText = (EditText) findViewById(R.id.playerName2);
                    player.setName(editText.getText().toString());
                    break;
                }
                case 2: {
                    EditText editText = (EditText) findViewById(R.id.playerName3);
                    player.setName(editText.getText().toString());
                    break;
                }
                case 3: {
                    EditText editText = (EditText) findViewById(R.id.playerName4);
                    player.setName(editText.getText().toString());
                    break;
                }
                default: {
                    throw new RuntimeException("wrong player index");
                }
            }
            game.getPlayers()[i] = player;

        }
        game.setStart(new Date());
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(Constants.PARAM_GAME, game); //Optional parameters
        Log.d(ACTION_TAG, "game was added " + game.toString());
        startActivity(intent);
        finish();
    }
}
