package space.fstudio.simplepaint.Objects;

import android.graphics.Point;

public class Points extends Point {

    public int x, y, width;
    private String color;

    public Points(int x, int y, int width, String color) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.width = width;
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
