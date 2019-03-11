package com.ratik.colorgram.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import com.ratik.colorgram.R
import com.ratik.colorgram.model.GramColor
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.File
import java.io.FileOutputStream

class DownloadHelper(private val context: Context) {
    private val TAG = DownloadHelper::class.java.simpleName

    private fun createResultBitmap(): Bitmap {
        return Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)
    }

    private fun getPaintedBitmap(color: GramColor): Bitmap {
        val bitmap = createResultBitmap()
        val canvas = Canvas(bitmap)
        canvas.drawARGB(255, color.red, color.green, color.blue)
        return bitmap
    }

    private fun sdCardPath() = Environment.getExternalStorageDirectory().absolutePath

    private fun createAndGetDownloadDestination(context: Context): File {
        val destinationPath = "${sdCardPath()}/${context.getString(R.string.app_name)}"
        val result = File(destinationPath)
        result.mkdirs()
        return result
    }

    fun downloadColor(color: GramColor): Deferred<File> {
        return GlobalScope.async {
            val paintedBitmap = getPaintedBitmap(color)
            val filename = "${color.red}_${color.green}_${color.blue}.jpg"
            val colorDownloadFile = File(createAndGetDownloadDestination(context), filename)

            try {
                val out = FileOutputStream(colorDownloadFile)
                paintedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
                colorDownloadFile
            } catch (e: Exception) {
                throw e
            }
        }
    }

    public fun broadcastSaveIntent(savedFile: File) {
        val scanFileIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(savedFile))
        context.sendBroadcast(scanFileIntent)
    }
}