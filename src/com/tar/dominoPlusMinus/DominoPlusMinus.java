package com.tar.dominoPlusMinus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DominoPlusMinus extends AbstractActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void newGame(View view) {
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
        finish();
    }

    public void loadGame(View view) {
        Intent intent = new Intent(this, LoadGameActivity.class);
        startActivity(intent);
        finish();
    }

    public void help(View view) {
        showToast(getString(R.string.under_construction));
    }

    public void changeLanguageToEnglish(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Внимание")
                .setMessage("функция пока не реализована").setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        })
                .show();
    }


}
