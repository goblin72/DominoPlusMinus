package com.tar.dominoPlusMinus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tar.dominoPlusMinus.model.Game;
import com.tar.dominoPlusMinus.model.Player;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * User: goblin72
 * Date: 13.02.2015
 * Time: 11:39
 */
public class LoadGameActivity extends AbstractActivity {
    private final String ACTION_TAG = "LoadGameActivity";
    private Game selectedGame;
    private LinearLayout selectedRow;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(ACTION_TAG, "onCreate");
        setContentView(R.layout.load_game);

        initTable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(ACTION_TAG, "onResume");
    }

    private void initTable()
    {
        selectedGame=null;
        selectedRow=null;
        GridLayout grid = (GridLayout) findViewById(R.id.load_games);
        if (grid.getChildCount()>0)
            grid.removeViews(0, grid.getChildCount());
        File[] files = getFilesDir().listFiles();
        Arrays.sort(files, new FileComparator());
        for (File file: files)
        {
            LinearLayout row = new LinearLayout(getApplicationContext());
            row.setBackgroundColor(getResources().getColor(R.color.black));
            try {
                Game game = readGameFromFile(file);
                row.setOnClickListener(new TableOnClickListener(game, row));
                TextView text = new TextView(getApplicationContext());
                text.setText(dateFormat.format(game.getStart()));
                text.setWidth(200);
                row.addView(text);

                TextView text1 = new TextView(getApplicationContext());
                text1.setText(String.valueOf(game.getRounds().size()));
                text1.setWidth(300);
                row.addView(text1);

                TextView text2 = new TextView(getApplicationContext());
                text2.setWidth(500);
                StringBuilder sb = new StringBuilder();
                for (Player player: game.getPlayers())
                {
                    if (sb.length()>0)
                        sb.append(", ");
                    sb.append(player.getName());
                }
                if (game.isFinished())
                    sb.append(" (").append(getString(R.string.game_over)).append(")");

                text2.setText(sb.toString());
                row.addView(text2);

                Log.d(ACTION_TAG, "saved game "+file.getName()+" "+text.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            grid.addView(row);
        }
    }

    public void back(View view) {
        Intent intent = new Intent(this, DominoPlusMinus.class);
        startActivity(intent);
        finish();
    }

    public void loadGame(View view)
    {
        if (selectedGame==null) {
            showToast(getString(R.string.game_is_not_selected));
            return;
        }
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(Constants.PARAM_GAME, selectedGame); //Optional parameters
        Log.d(ACTION_TAG, "game was added " + selectedGame.toString());
        startActivity(intent);
        finish();

    }

    public void deleteGame(View view)
    {
        if (selectedGame==null) {
            showToast(getString(R.string.game_is_not_selected));
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.attention))
                .setMessage(getString(R.string.delete_game_confirm)+" "+dateFormat.format(selectedGame.getStart()))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        File[] files = getFilesDir().listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File file, String s) {

                                return s.equals("" + selectedGame.getStart().getTime());
                            }
                        });
                        if (files != null && files.length == 1) {
                            files[0].delete();
                        }
                        initTable();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private class TableOnClickListener implements View.OnClickListener
    {
        Game game;
        LinearLayout row;

        public TableOnClickListener(Game game, LinearLayout row) {
            this.game=game;
            this.row=row;
        }

        @Override
        public void onClick(View view) {
            if (selectedRow!=null)
                selectedRow.setBackgroundColor(getResources().getColor(R.color.black));
            row.setBackgroundColor(getResources().getColor(R.color.olive));
            selectedGame=game;
            selectedRow=row;
        }
    }
}
