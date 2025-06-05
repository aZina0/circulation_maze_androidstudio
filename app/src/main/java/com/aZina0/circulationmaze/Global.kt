package com.aZina0.circulationmaze

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.ByteArrayOutputStream

object Global {
    var redrawAmount = 0
    var screenWidthDp: Int? = null
    var screenHeightDp: Int? = null

    fun print(text: String) {
        Log.d("CustomPrint", text)
    }

    fun relativeHeight(percent: Float): Dp {
        return (screenHeightDp!! * percent).dp
    }

    fun relativeWidth(percent: Float): Dp {
        return (screenWidthDp!! * percent).dp
    }

    fun relativeFont(percent: Float): TextUnit {
        return (screenHeightDp!! * percent).sp
    }

    fun byteStringToImageBitmap(byteString: String): ImageBitmap {
        val imageBytes = Base64.decode(byteString, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return bitmap.asImageBitmap()
    }

    fun imageBitmapToByteString(imageBitmap: ImageBitmap): String {
        val bitmap = imageBitmap.asAndroidBitmap()
        val byteStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream)
        val bytes = byteStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}