package space.fstudio.simplepaint.Objects

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.widget.EditText
import space.fstudio.simplepaint.R
import java.io.File
import java.io.FileOutputStream

class FileOperations(private val ac: Activity) {

    private lateinit var tr: Thread
    private lateinit var bMap: Bitmap
    var filename: String? = null

    fun saveBitmapAs(bitmap: Bitmap, context: Context) {
        bMap = Bitmap.createBitmap(bitmap)
        saveDialog(context)
    }

    fun saveBitmap(bitmap: Bitmap, context: Context) {
        if (this.filename == null) {
            saveBitmapAs(bitmap, context)
            return
        }
        bMap = Bitmap.createBitmap(bitmap)
        lateinit var file: File
        try {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), this.filename.toString() + ".png")
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

    private fun saveDialog(context: Context) {
        val filename = EditText(context)
        val builder = AlertDialog.Builder(context)
        builder.setView(filename)
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.setPositiveButton("Save") { _, _ ->

            lateinit var file: File
            try {
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    file = if (filename.text.contains(".png"))
                        File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename.text.toString())
                    else File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename.text.toString() + ".png")
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
                this.filename = if (filename.text.toString().contains(".png"))
                    filename.text.toString().substringBefore(".png")
                else filename.text.toString()
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

    fun requestBMap() {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            //            val uri = Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata%2Fspace.fstudio.simplepaint%2Ffiles%2FPictures")
//            putExtra("EXTRA_INITIAL_URI", uri)
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/png"
//            println(uri)

        }

        ac.startActivityForResult(Intent.createChooser(intent, "Choose a file."), 122)

    }

    fun loadBitmap(context: Context, data: Uri): Bitmap {
        data.also { uri ->
            val opts = BitmapFactory.Options().apply { inMutable = true }
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            bMap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, opts)
            parcelFileDescriptor?.close()
            context.contentResolver.query(uri, null, null, null, null)
                    ?.use { cursor ->
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        cursor.moveToFirst()
                        this.filename = cursor.getString(nameIndex).substringBefore(".png")
                    }
        }
//        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "filename.png")
//        println("Dose file exist = " + file.exists())
//        val opts = BitmapFactory.Options()
//        opts.inMutable = true
//        if (file.exists()) {
//            bMap = BitmapFactory.decodeFile(file.path, opts)
//        } else return null
        println("Filename is: " + this.filename)
        return bMap
    }

}