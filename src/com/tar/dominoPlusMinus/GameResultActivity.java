package com.tar.dominoPlusMinus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tar.dominoPlusMinus.model.Statistics;

import java.text.SimpleDateFormat;

/**
 * User: goblin72
 * Date: 11.02.2015
 * Time: 12:52
 */
public class GameResultActivity extends AbstractActivity {

    private final String ACTION_TAG = "GameResultActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_results);
        Log.d(ACTION_TAG, "onCreate");
        Intent intent = getIntent();

        Statistics stat = (Statistics) intent.getSerializableExtra(Constants.PARAM_STAT);
        fillStatistics(stat);
    }

    private void fillStatistics(Statistics stat)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sb=new StringBuilder();
        sb.append(getString(R.string.stat_report_1)).append(format.format(stat.getStart())).
                append(getString(R.string.stat_report_winner)).append(stat.getWinner()).
                append(getString(R.string.stat_report_rounds)).append(stat.getRounds());
        TextView mainStat = (TextView) findViewById(R.id.stat_header);
        mainStat.setText(sb.toString());

        GridLayout grid = (GridLayout) findViewById(R.id.stat_grid);

        grid.setColumnCount(stat.getPlayers().length + 1);

        addLine(grid, getString(R.string.stat_report_player), stat.getPlayers());
        addLine(grid, getString(R.string.stat_report_points), stat.getPoints());
        addLine(grid, getString(R.string.stat_report_score), stat.getScore());
        addLine(grid, getString(R.string.stat_report_score_earned), stat.getScoreEarned());
        addLine(grid, getString(R.string.stat_report_score_spent), stat.getScoreSpent());
        addLine(grid, getString(R.string.stat_report_wins), stat.getWins());
        addLine(grid, getString(R.string.stat_report_fishes), stat.getFishes());
        addLine(grid, getString(R.string.stat_report_palindrome), stat.getPalindromes());
        addLine(grid, getString(R.string.stat_report_antipalindrome), stat.getAntiPalindromes());
        addLine(grid, getString(R.string.stat_report_max_points), stat.getMaxDelta());
        addLine(grid, getString(R.string.stat_report_max_points2), stat.getMaxPoints());
    }


    private void addLine(GridLayout grid, String header, Integer[] data)
    {
        addCell(grid, header);

        for (Integer cell: data)
        {
            addCell(grid, String.valueOf(cell));
        }
    }

    private void addCell(GridLayout grid, String text)
    {
        TextView cell = new TextView(grid.getContext());
        cell.setText(text);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cell.setLayoutParams(params);
        cell.setGravity(Gravity.CENTER_VERTICAL);

        cell.setPadding(0, 20, 40, 20);
        cell.setTextSize(20);
        grid.addView(cell);
    }

    private void addLine(GridLayout grid, String header, String[] data)
    {
        addCell(grid, header);

        for (String cell: data)
        {
            addCell(grid, cell);
        }
    }

    public void menu(View view) {
        Intent intent = new Intent(this, DominoPlusMinus.class);
        startActivity(intent);
        finish();
    }
}
