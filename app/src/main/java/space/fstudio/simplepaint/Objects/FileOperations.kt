package space.fstudio.simplepaint.Objects

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.widget.EditText
import space.fstudio.simplepaint.R
import java.io.File
import java.io.FileOutputStream

class FileOperations {

    private lateinit var tr: Thread
    private lateinit var bMap: Bitmap
    fun saveBitmap(bitmap: Bitmap, context: Context) {
        bMap = Bitmap.createBitmap(bitmap)
        saveDialog(context)
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

    private fun saveDialog(context: Context) {
        val filename = EditText(context)
        val builder = AlertDialog.Builder(context)
        builder.setView(filename)
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.setPositiveButton("Save") { _, _ ->

            lateinit var file: File
            try {
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename.text.toString() + ".png")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (file.exists()) {
                val builder2 = AlertDialog.Builder(context)
                builder2.setTitle("File already exists!")
                builder2.setMessage("Do you want to overwrite it?")
                builder2.setNegativeButton("Cancel") { _, _ -> }
                builder2.setPositiveButton("Overwrite") { _, _ ->
                    tr = Thread(Runnable {
                        val oStream = FileOutputStream(file)
                        bMap.compress(Bitmap.CompressFormat.PNG, 10, oStream)
                        oStream.close()
                    })
                    tr.start()
                }
                builder2.create().show()
            } else {
                tr = Thread(Runnable {
                    val oStream = FileOutputStream(file)
                    bMap.compress(Bitmap.CompressFormat.PNG, 10, oStream)
                    oStream.close()
                })
                tr.start()
            }

        }
        builder.setTitle("Save")
        builder.setMessage("Give a filename")
        builder.setIcon(R.drawable.ic_save_black_24dp)

        builder.create().show()
    }
}