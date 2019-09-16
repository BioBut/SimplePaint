package space.fstudio.simplepaint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import space.fstudio.simplepaint.Objects.PrefUtils;
import space.fstudio.simplepaint.Views.SimpleDrawingView;
import space.fstudio.simplepaint.Views.VerticalSeekBar;

public class MainActivity extends AppCompatActivity {

    long exitTime;
    private VerticalSeekBar widthBar;
    private SimpleDrawingView simpleDrawingView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        widthBar = findViewById(R.id.widthBar);

        simpleDrawingView = findViewById(R.id.simpleDrawingView1);

        simpleDrawingView.setActivity(this);

        widthBar.setThumb(getThumb(1));
        widthBar.setProgress(1);

        if (widthBar.getProgress() == 0) {
            widthBar.setProgress(1);
        }

        widthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                simpleDrawingView.setWidth(i);
                widthBar.setThumb(getThumb(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.getItem(4).getIcon().setColorFilter(new PrefUtils().loadMenuColor(this));
        return true;
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 1500) {
            exitTime = System.currentTimeMillis();
            Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Permission allowed");
            } else {
                Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveMenuBtn:
                simpleDrawingView.saveImage();
                return true;
            case R.id.saveAsMenuBtn:
                simpleDrawingView.saveImageAs();
                return true;
            case R.id.loadMenuBtn:
                simpleDrawingView.loadImage();
                return true;
            case R.id.clearMenuBtn:
                simpleDrawingView.clearCanvas();
                return true;
            case R.id.colorPiker:
                simpleDrawingView.colorPickerDialog(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Drawable getThumb(int progress) {

        @SuppressLint("InflateParams")
        View thumbView = LayoutInflater.from(this).inflate(R.layout.layout_seekbar_thumb, null, false);
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 122 && resultCode == RESULT_OK)
            if (data != null) {
                simpleDrawingView.setBMap(data.getData());
            }
    }
}
