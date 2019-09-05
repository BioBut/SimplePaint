package space.fstudio.simplepaint.Examples;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import space.fstudio.simplepaint.R;

public class Dialogs extends View {
    public Dialogs(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        EditText editText = new EditText(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(editText);

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setTitle("TItle");
        builder.setMessage("Save the file");
        builder.setIcon(R.drawable.circle);

        builder.create().show();
    }
}
