package com.tar.dominoPlusMinus;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import com.tar.dominoPlusMinus.model.Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Comparator;

/**
 * User: goblin72
 * Date: 11.02.2015
 * Time: 11:59
 */
public abstract class AbstractActivity extends Activity {

    protected void showToast(CharSequence message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    protected Game readGameFromFile(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        return (Game) ois.readObject();
    }

    protected class FileComparator implements Comparator<File>
    {
        @Override
        public int compare(File file, File anotherFile) {
            return -file.getName().compareTo(anotherFile.getName());
        }
    }

    public String getStringScore(int score) {
        int stick = score % 2;
        int pluses = score / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pluses; i++)
            sb.append(getString(R.string.plus));
        if (stick == 1)
            sb.append(getString(R.string.stick));
        return sb.toString();
    }
}
