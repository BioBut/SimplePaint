package space.fstudio.simplepaint.Objects

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

class FileOperations {

    lateinit var tr: Thread
    lateinit var bMap: Bitmap
    fun saveBitmap(bitmap: Bitmap, file: File) {
        bMap = Bitmap.createBitmap(bitmap)
        tr = Thread(Runnable {
            val oStream = FileOutputStream(file)
            bMap.compress(Bitmap.CompressFormat.PNG, 10, oStream)
            oStream.close()
        })
        tr.start()
    }
}