package space.fstudio.simplepaint.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import space.fstudio.simplepaint.Objects.Points;

public class SimpleDrawingView extends View {

    Paint paint;
    List<Points> points = new ArrayList<>();
    String color;
    int width = 1;
    Bitmap bMap;


    public SimpleDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    public void setupPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setHexColor(String color) {
        this.color = color;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void clearCanvas() {
        bMap = null;
        points.clear();
        invalidate();
    }

    public void saveImage() {
        try {
            setDrawingCacheEnabled(true);
            Bitmap bitmap = getDrawingCache();
            File f = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                f = new File(Environment.getExternalStorageDirectory() + File.separator + "filename" + ".png");
            }
            FileOutputStream ostream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
            ostream.close();
            setDrawingCacheEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImage() {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "filename" + ".png");
        System.out.println("Dose file exist = " + file.exists());
        if (file.exists()) {
            bMap = BitmapFactory.decodeFile(file.getPath());
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bMap != null)
            canvas.drawBitmap(bMap, 0, 0, paint);
        for (Points p : points) {
            paint.setColor(Color.parseColor(p.getColor()));
            canvas.drawCircle(p.x, p.y, p.width, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float touchX = event.getX();
        float touchY = event.getY();

        if (color == null)
            Toast.makeText(getContext(), "Please select a color", Toast.LENGTH_SHORT).show();
        else
            points.add(new Points((int) touchX, (int) touchY, width, color));

        invalidate();
        return true;
    }
}

