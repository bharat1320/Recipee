package com.project.recipee.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.TypeConverter
import com.project.recipee.data.Nutrient
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


class Converters {

    @TypeConverter
    fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        var baos: ByteArrayOutputStream? = null
        return try {
            baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            baos.toByteArray()
        } finally {
            if (baos != null) {
                try {
                    baos.close()
                } catch (e: Exception) {
                    Log.e("Bitmap to ByteArray",e.toString())
                }
            }
        }
    }

    @TypeConverter
    fun convertCompressedByteArrayToBitmap(src: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(src, 0, src.size)
    }

    @TypeConverter
    fun convertArrayListToByteArray(data : ArrayList<Nutrient>): ByteArray{
//        val size: Int = data.size
//        val byteArray = ByteArray(size)
//        for (i in 0 until size) {
//            byteArray[i] = java.lang.Byte.valueOf(data[i])
//        }
//        return byteArray
        val outputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(outputStream)
        objectOutputStream.writeObject(data)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun convertByteArrayToArrayList(data : ByteArray): ArrayList<Nutrient>{
//        val output = arrayListOf<Nutrient>()
//        for( i in data) {
//            output.add(i)
//        }
//        return output
        val bytes: ByteArray = data
        val customList: ArrayList<Nutrient> = ArrayList()
        val inputStream = ByteArrayInputStream(bytes)
        val objectInputStream = ObjectInputStream(inputStream)
        while (inputStream.available() > 0) {
            val obj = objectInputStream.readObject() as Nutrient
            customList.add(obj)
        }
        return customList
    }
}