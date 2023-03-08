package com.project.recipee.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.recipee.data.Nutrient
import com.project.recipee.data.Nutrition
import java.io.*


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
    fun convertByteArrayToBitmap(src: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(src, 0, src.size)
    }

    @TypeConverter
    fun convertArrayListObjectToByteArray(data : ArrayList<Nutrient>): ByteArray{
        val outputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(outputStream)
        objectOutputStream.writeObject(data)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun convertByteArrayToArrayListObject(data : ByteArray): ArrayList<Nutrient>{
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

//    @TypeConverter
//    fun convertArrayListStringToByteArray(data : ArrayList<String>): ByteArray{
//        val outputStream = ByteArrayOutputStream()
//        for (string in data) {
//            outputStream.write(string.toByteArray())
//        }
//        return outputStream.toByteArray()
//    }
//
//    @TypeConverter
//    fun convertByteArrayToArrayListString (data : ByteArray): ArrayList<String>{
////        val output = arrayListOf<String>()
////        for (byte in data) {
////            val str = String(byteArrayOf(byte))
////            output.add(str)
////        }
////        return output
//        val output = arrayListOf<String>()
//        val bais = ByteArrayInputStream(data)
//        val inter = DataInputStream(bais)
//        while (inter.available() > 0) {
//            output.add(inter.readUTF())
//        }
//        return output
//    }

//    @TypeConverter
//    fun fromString(value: String): ArrayList<String> {
//        val listType = object : TypeToken<ArrayList<String>>() {}.type
//        return Gson().fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun fromArrayList(list: ArrayList<String>): String {
//        return Gson().toJson(list)
//    }

    @TypeConverter
    fun convertCustomObjectToByteArray (data : Nutrition): ByteArray{
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(data)
        return byteArrayOutputStream.toByteArray()
    }

    @TypeConverter
    fun convertByteArrayToCustomObject (data : ByteArray): Nutrition{
        val byteArrayInputStream = ByteArrayInputStream(data)
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as Nutrition
    }

    @TypeConverter
    fun fromByteArray(value: ByteArray?): ArrayList<String>? {
        if (value == null) {
            return null
        }
        val bis = ByteArrayInputStream(value)
        var `in`: ObjectInput? = null
        val arrayList: ArrayList<String> = ArrayList()
        try {
            `in` = ObjectInputStream(bis)
            val size = `in`.readInt()
            for (i in 0 until size) {
                arrayList.add(`in`.readUTF())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `in`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return arrayList
    }

    @TypeConverter
    fun toByteArray(arrayList: ArrayList<String>?): ByteArray? {
        if (arrayList == null) {
            return null
        }
        val bos = ByteArrayOutputStream()
        var out: ObjectOutput? = null
        return try {
            out = ObjectOutputStream(bos)
            out.writeInt(arrayList.size)
            for (s in arrayList) {
                out.writeUTF(s)
            }
            out.flush()
            bos.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            try {
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}

//@TypeConverter
//fun convertArrayListStringToByteArray(data : ArrayList<String>): ByteArray{
//    val outputStream = ByteArrayOutputStream()
//    val out = DataOutputStream(outputStream)
//    for (string in data) {
////            outputStream.write(string.toByteArray())
//        out.writeUTF(string)
//    }
//    return outputStream.toByteArray()
//
////        ByteArrayOutputStream baos = new ByteArrayOutputStream();
////        DataOutputStream out = new DataOutputStream(baos);
////        for (String element : list) {
////            out.writeUTF(element);
////        }
////        byte[] bytes = baos.toByteArray()
//}
//
//@TypeConverter
//fun convertByteArrayToArrayListString (data : ByteArray): ArrayList<String>{
////        val output = arrayListOf<String>()
////        for (byte in data) {
////            val str = String(byteArrayOf(byte))
////            output.add(str)
////        }
////        return output
//    val output = arrayListOf<String>()
//    val bais = ByteArrayInputStream(data);
//    val inter = DataInputStream(bais);
//    while (inter.available() > 0) {
//        output.add(inter.readUTF())
//    }
//    return output
//}