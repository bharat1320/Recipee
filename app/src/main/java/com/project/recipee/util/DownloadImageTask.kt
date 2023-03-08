package com.project.recipee.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageTask(@SuppressLint("StaticFieldLeak") private val context: Context, private val imageUrl: String) : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void?): String {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val fileName = "image_${System.currentTimeMillis()}.jpg"
            val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File(directory, fileName)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            return file.absolutePath
        } catch (e: Exception) {
            Log.e("DownloadImageTask", "Error downloading image", e)
        }
        return ""
    }
}