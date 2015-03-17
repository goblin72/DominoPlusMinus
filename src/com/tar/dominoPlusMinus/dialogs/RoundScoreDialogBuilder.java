package com.tar.dominoPlusMinus.dialogs;

import android.content.DialogInterface;
import android.view.View;
import android.widget.*;
import com.tar.dominoPlusMinus.GameActivity;
import com.tar.dominoPlusMinus.R;
import com.tar.dominoPlusMinus.model.Player;
import com.tar.dominoPlusMinus.scoreOptions.RoundScoreOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * User: goblin72
 * Date: 12.02.2015
 * Time: 12:06
 */
public class RoundScoreDialogBuilder extends AbstractDialogBuilder {

    private static GameActivity context;

    public RoundScoreDialogBuilder(final int playerIndex) {
        super(context);

        LinearLayout layout = new LinearLayout(context);

        TextView label1 = new TextView(context);
        label1.setText(context.getString(R.string.available_score_options));
        layout.addView(label1);

        final RadioGroup scoreOptionsGroup = new RadioGroup(context);

        final List<RoundScoreOptions> options = getApplicable(playerIndex);

        final TextView label2 = new TextView(context);
        final RadioGroup subjectsGroup = new RadioGroup(context);

        final RadioButton[] scoreOptions = new RadioButton[options.size()];
        scoreOptionsGroup.setOrientation(RadioGroup.VERTICAL);
        for(int index=0; index<options.size(); index++){
            final RoundScoreOptions currentOption = options.get(index);

            scoreOptions[index]  = new RadioButton(context);
            scoreOptionsGroup.addView(scoreOptions[index]);
            scoreOptions[index].setText(context.getString(currentOption.nameId)+" ("+context.getStringScore(currentOption.costs)+")");

            scoreOptions[index].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    int index=0;
                    for (RadioButton button : scoreOptions) {
                        if (button.isChecked()) {
                            if (options.get(index).needSubject)
                            {
                                label2.setVisibility(View.VISIBLE);
                                subjectsGroup.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                label2.setVisibility(View.INVISIBLE);
                                subjectsGroup.setVisibility(View.INVISIBLE);
                            }
                        }
                        index++;
                    }
                }
            });



        }
        layout.addView(scoreOptionsGroup);
        label2.setVisibility(View.INVISIBLE);
        subjectsGroup.setVisibility(View.INVISIBLE);

        label2.setText(context.getString(R.string.score_options_aim));
        layout.addView(label2);


        final RadioButton[] subjects = new RadioButton[context.getCurrentGame().getPlayers().length];
        subjectsGroup.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for(int index=0; index<context.getCurrentGame().getPlayers().length; index++){
            subjects[index]  = new RadioButton(context);
            subjectsGroup.addView(subjects[index]);
            subjects[index].setText(context.getCurrentGame().getPlayers()[index].getName());
        }
        layout.addView(subjectsGroup);


        this.setView(layout);


        final Player clicker = context.getCurrentGame().getPlayers()[playerIndex];
        this.setPositiveButton(context.getString(R.string.score_use), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int index=0;
                int subjectIndex=0;
                for (RadioButton button : scoreOptions) {

                    if (button.isChecked()) {

                        RoundScoreOptions selectedOption =options.get(index);
                        if (selectedOption.needSubject)
                        {

                            boolean subjectSelected=false;
                            for (RadioButton subject : subjects) {
                                if (subject.isChecked()) {
                                    subjectSelected=true;
                                    break;
                                }
                                subjectIndex++;
                            }

                            if (!subjectSelected) {
                                showToast(context.getString(R.string.select_aim));
                                return;
                            }
                        }
                        options.get(index).executeScoreOption(playerIndex, subjectIndex);
                    }
                    index++;
                }
            }
        });

        this.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        setTitle(context.getString(R.string.score_spend_dialog_1) + " " + clicker.getName() +"\n"+ context.getString(R.string.score_spend_dialog_2) + " " + context.getStringScore(clicker.getScore()));
    }

    private List<RoundScoreOptions> getApplicable(int playerIndex)
    {
        List<RoundScoreOptions> result = new ArrayList<RoundScoreOptions>();
        Player player = context.getCurrentGame().getPlayers()[playerIndex];
        for (RoundScoreOptions item: RoundScoreOptions.values())
        {
            if (item.costs>player.getScore())
                continue;
            if (item.condition!=null && !item.condition.equals(context.getCurrentSign()))
                continue;
            result.add(item);
        }
        return result;

    }

    public static void setContext(GameActivity context) {
        RoundScoreDialogBuilder.context = context;
    }
}
