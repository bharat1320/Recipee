package com.project.recipee.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL


class CommonFunctions {
    companion object{

        fun getBitmapByUrl(context :Context,url :String): Bitmap? {
            return try {
                BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
            } catch (e: Exception) {
                null
            }
        }

    }
}