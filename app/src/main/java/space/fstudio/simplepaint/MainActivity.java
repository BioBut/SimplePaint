package space.fstudio.simplepaint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import space.fstudio.simplepaint.Views.SimpleDrawingView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button redColorBtn, yellowColorBtn, greenColorBtn, blueColorBtn, purpleColorBtn, blackColorBtn;
    SeekBar widthBar;
    SimpleDrawingView simpleDrawingView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        colorSelection();

        widthBar = findViewById(R.id.widthBar);

        simpleDrawingView = findViewById(R.id.simpleDrawingView1);

        if (widthBar.getProgress() == 0)
            widthBar.setProgress(1);
        widthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                simpleDrawingView.setWidth(i);
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
        inflater.inflate(R.layout.menu, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.saveMenuBtn:
                simpleDrawingView.saveImage();
                return true;
            case R.id.loadMenuBtn:
                simpleDrawingView.loadImage();
                return true;
            case R.id.clearMenuBtn:
                simpleDrawingView.clearCanvas();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void colorSelection() {
        redColorBtn = findViewById(R.id.redColorBtn);
        redColorBtn.setOnClickListener(this);

        yellowColorBtn = findViewById(R.id.yellowColorBtn);
        yellowColorBtn.setOnClickListener(this);

        greenColorBtn = findViewById(R.id.greenColorBtn);
        greenColorBtn.setOnClickListener(this);

        blueColorBtn = findViewById(R.id.blueColorBtn);
        blueColorBtn.setOnClickListener(this);

        purpleColorBtn = findViewById(R.id.purpleColorBtn);
        purpleColorBtn.setOnClickListener(this);

        blackColorBtn = findViewById(R.id.blackColorBtn);
        blackColorBtn.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.redColorBtn:
                simpleDrawingView.setHexColor("#e74c3c");
                break;
            case R.id.yellowColorBtn:
                simpleDrawingView.setHexColor("#f1c40f");
                break;
            case R.id.greenColorBtn:
                simpleDrawingView.setHexColor("#2ecc71");
                break;
            case R.id.blueColorBtn:
                simpleDrawingView.setHexColor("#3498db");
                break;
            case R.id.purpleColorBtn:
                simpleDrawingView.setHexColor("#9b59b6");
                break;
            case R.id.blackColorBtn:
                simpleDrawingView.setHexColor("#000000");
                break;
        }
    }
}
