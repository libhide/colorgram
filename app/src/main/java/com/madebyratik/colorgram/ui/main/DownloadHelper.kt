package com.madebyratik.colorgram.ui.main

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.madebyratik.colorgram.R
import com.madebyratik.colorgram.model.GramColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DownloadHelper(private val context: Context) {
    private fun getPaintedBitmap(color: GramColor): Bitmap {
        val bitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)
        Canvas(bitmap).drawARGB(255, color.red, color.green, color.blue)
        return bitmap
    }

    suspend fun downloadColor(color: GramColor) = withContext(Dispatchers.IO) {
        val bitmap = getPaintedBitmap(color)
        val filename = "${color.red}_${color.green}_${color.blue}.jpg"
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveWithMediaStore(bitmap, filename)
            } else {
                saveToLegacyStorage(bitmap, filename)
            }
        } finally {
            bitmap.recycle()
        }
    }

    private fun saveWithMediaStore(bitmap: Bitmap, filename: String) {
        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "${Environment.DIRECTORY_PICTURES}/${context.getString(R.string.app_name)}",
            )
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: throw IOException("Unable to create image in MediaStore")

        try {
            resolver.openOutputStream(uri)?.use { output ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)) {
                    throw IOException("Unable to encode image")
                }
            } ?: throw IOException("Unable to open image output stream")

            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
        } catch (exception: Exception) {
            resolver.delete(uri, null, null)
            throw exception
        }
    }

    @Suppress("DEPRECATION")
    private fun saveToLegacyStorage(bitmap: Bitmap, filename: String) {
        val destination = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            context.getString(R.string.app_name),
        ).apply {
            if (!exists() && !mkdirs()) {
                throw IOException("Unable to create image directory")
            }
        }
        val image = File(destination, filename)
        FileOutputStream(image).use { output ->
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)) {
                throw IOException("Unable to encode image")
            }
        }
        MediaScannerConnection.scanFile(context, arrayOf(image.absolutePath), arrayOf("image/jpeg"), null)
    }
}
