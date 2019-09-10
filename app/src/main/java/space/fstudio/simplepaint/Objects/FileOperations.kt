package space.fstudio.simplepaint.Objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

class FileOperations {

    private lateinit var tr: Thread
    private lateinit var bMap: Bitmap
    fun saveBitmap(bitmap: Bitmap, context: Context) {
        bMap = Bitmap.createBitmap(bitmap)
        lateinit var file: File
        try {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "filename.png")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        tr = Thread(Runnable {
            val oStream = FileOutputStream(file)
            bMap.compress(Bitmap.CompressFormat.PNG, 10, oStream)
            oStream.close()
        })
        tr.start()
    }

    fun loadBitmap(context: Context): Bitmap? {

        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "filename.png")
        println("Dose file exist = " + file.exists())
        val opts = BitmapFactory.Options()
        opts.inMutable = true
        if (file.exists()) {
            bMap = BitmapFactory.decodeFile(file.path, opts)
        } else return null
        return bMap
    }
}