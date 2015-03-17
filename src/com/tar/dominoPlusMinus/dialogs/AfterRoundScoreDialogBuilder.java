package com.tar.dominoPlusMinus.dialogs;

import android.content.DialogInterface;
import android.widget.*;
import com.tar.dominoPlusMinus.GameActivity;
import com.tar.dominoPlusMinus.R;
import com.tar.dominoPlusMinus.RoundResultsActivity;
import com.tar.dominoPlusMinus.model.Player;
import com.tar.dominoPlusMinus.scoreOptions.AfterRoundScoreOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * User: goblin72
 * Date: 12.02.2015
 * Time: 12:06
 */
public class AfterRoundScoreDialogBuilder extends AbstractDialogBuilder {

    public AfterRoundScoreDialogBuilder(final RoundResultsActivity currentContext) {
        super(currentContext);

        final GameActivity context =GameActivity.getInstance();

        LinearLayout layout = new LinearLayout(context);


        final RadioGroup scoreOptionsGroup = new RadioGroup(context);

        final List<AfterRoundScoreOptions> options = getApplicable();

        final RadioGroup subjectsGroup = new RadioGroup(context);

        final TextView label2 = new TextView(context);
        label2.setText(context.getString(R.string.score_selector));
        layout.addView(label2);


        final RadioButton[] subjects = new RadioButton[context.getCurrentGame().getPlayers().length];
        subjectsGroup.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for(int index=0; index<context.getCurrentGame().getPlayers().length; index++){
            subjects[index]  = new RadioButton(context);
            subjectsGroup.addView(subjects[index]);
            subjects[index].setText(context.getCurrentGame().getPlayers()[index].getName());
        }
        layout.addView(subjectsGroup);


        TextView label1 = new TextView(context);
        label1.setText(context.getString(R.string.available_score_options));
        layout.addView(label1);

        final RadioButton[] scoreOptions = new RadioButton[options.size()];
        scoreOptionsGroup.setOrientation(RadioGroup.VERTICAL);
        for(int index=0; index<options.size(); index++){
            final AfterRoundScoreOptions currentOption = options.get(index);

            scoreOptions[index]  = new RadioButton(context);
            scoreOptionsGroup.addView(scoreOptions[index]);
            scoreOptions[index].setText(context.getString(currentOption.nameId)+" ("+context.getStringScore(currentOption.costs)+")");
        }
        layout.addView(scoreOptionsGroup);

        this.setView(layout);

        this.setPositiveButton(context.getString(R.string.score_use), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int index=0;
                int subjectIndex=0;

                boolean subjectSelected=false;
                for (RadioButton subject : subjects) {
                    if (subject.isChecked()) {
                        subjectSelected=true;
                        break;
                    }
                    subjectIndex++;
                }

                if (!subjectSelected) {
                    showToast(context.getString(R.string.select_score_selector));
                    return;
                }


                for (RadioButton button : scoreOptions) {

                    if (button.isChecked()) {

                        AfterRoundScoreOptions selectedOption =options.get(index);
                        Player player = context.getCurrentGame().getPlayers()[subjectIndex];

                        if (player.getScore()<selectedOption.costs)
                        {
                            showToast(context.getString(R.string.not_enough_score));
                            return;
                        }

                        options.get(index).executeScoreOption(context,currentContext, subjectIndex);
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

        setTitle(context.getString(R.string.score_spend_dialog_3));

    }

    private List<AfterRoundScoreOptions> getApplicable()
    {
        final GameActivity context =GameActivity.getInstance();
        List<AfterRoundScoreOptions> result = new ArrayList<AfterRoundScoreOptions>();
        for (AfterRoundScoreOptions item: AfterRoundScoreOptions.values())
        {
             if (item.maxNumberOfPlayers<context.getCurrentGame().getPlayers().length)
                 continue;

            result.add(item);
        }
        return result;

    }
}
