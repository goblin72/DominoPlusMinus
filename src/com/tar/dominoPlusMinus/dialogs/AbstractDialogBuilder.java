package com.tar.dominoPlusMinus.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * User: goblin72
 * Date: 12.02.2015
 * Time: 12:08
 */
public abstract class AbstractDialogBuilder  extends AlertDialog.Builder
{
    public AbstractDialogBuilder(Context context) {
        super(context);
    }

    protected void showToast(CharSequence message) {
        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
