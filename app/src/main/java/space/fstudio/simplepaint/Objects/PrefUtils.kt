package space.fstudio.simplepaint.Objects

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter


class PrefUtils {


    fun loadMenuColor(context: Context): ColorFilter {
        val sPref = context.getSharedPreferences("COLOR", MODE_PRIVATE)
        return PorterDuffColorFilter(sPref.getInt("selectedColor", 0), PorterDuff.Mode.SRC_ATOP)
    }
}