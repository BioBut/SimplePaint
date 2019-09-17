package space.fstudio.simplepaint.Objects

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter


class PrefUtils {

    companion object Methods {
        private const val PREFERENCE_NAME = "COLOR"

        private const val COLOR_KEY = "selectedColor"
        fun loadMenuColor(context: Context): ColorFilter {
            val sPref = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
            return PorterDuffColorFilter(sPref.getInt(COLOR_KEY, 0), PorterDuff.Mode.SRC_ATOP)
        }

        fun loadColor(context: Context): Int {
            val sPref = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
            return sPref.getInt(COLOR_KEY, Color.BLACK)
        }

        fun saveColor(color: Int, context: Context) {
            val editor = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE).edit()
            editor.putInt(COLOR_KEY, color)
            editor.apply()
        }

        private const val STROKE_WIDTH_KEY = "strokeWidth"
        fun saveWidth(width: Int, context: Context) {
            val editor = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE).edit()
            editor.putInt(STROKE_WIDTH_KEY, width)
            editor.apply()
        }

        fun loadWidth(context: Context): Int {
            val sPref = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
            return sPref.getInt(STROKE_WIDTH_KEY, 1)
        }
    }
}