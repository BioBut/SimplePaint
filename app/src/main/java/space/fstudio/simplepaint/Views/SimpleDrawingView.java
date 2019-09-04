package space.fstudio.simplepaint.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;

public class SimpleDrawingView extends View {

    Paint mPaint;
    String color;
    int width = 1;
    Bitmap bMap;
    Path mPath;
    Paint mBitmapPaint;
    Canvas mCanvas;
    private int cHeight;
    private int cWidth;


    public SimpleDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    public void setupPaint() {
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    public void setHexColor(String color) {
        mPaint.setColor(Color.parseColor(color));
        this.color = color;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void clearCanvas() {
        bMap = Bitmap.createBitmap(cWidth, cHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bMap);
        invalidate();
    }

    @SuppressLint("WrongThread")
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
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inMutable = true;
        if (file.exists()) {
            bMap = BitmapFactory.decodeFile(file.getPath(), opts);
            mCanvas = new Canvas(bMap);
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        this.cWidth = w;
        this.cHeight = h;

        bMap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bMap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(this.width);
        if (bMap != null) {
            canvas.drawBitmap(bMap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();

        if (color == null)
            Toast.makeText(getContext(), "Please select a color", Toast.LENGTH_SHORT).show();
        else
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }

        return true;
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }
}

