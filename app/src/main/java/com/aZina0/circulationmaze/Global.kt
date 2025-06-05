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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    fun gameDataToString(gameData: GameData): String {
        return JsonObject(
            mapOf(
                "uid" to JsonPrimitive(gameData.uid),
                "seed" to JsonPrimitive(gameData.seed),
                "gridSize" to JsonPrimitive(gameData.gridSize),
                "lastModifiedDate" to JsonPrimitive(
                    gameData.lastModifiedDate.format(DateTimeFormatter.ISO_DATE_TIME)
                ),
                "savedOnCloud" to JsonPrimitive(gameData.savedOnCloud),
                "pieces" to gameData.pieces,
            )
        ).toString()
    }

    fun stringToGameData(string: String): GameData {
        val jsonObject = Json.parseToJsonElement(string).jsonObject
        return GameData(
            uid = jsonObject["uid"]!!.jsonPrimitive.content,
            seed = jsonObject["seed"]!!.jsonPrimitive.long,
            gridSize = jsonObject["gridSize"]!!.jsonPrimitive.int,
            lastModifiedDate = LocalDateTime.parse(
                jsonObject["lastModifiedDate"]!!.jsonPrimitive.content,
                DateTimeFormatter.ISO_DATE_TIME
            ),
            savedOnCloud = jsonObject["savedOnCloud"]!!.jsonPrimitive.boolean,
            pieces = jsonObject["pieces"]!!.jsonObject
        )
    }

    fun getCroppedScaledImageBitmap(bitmap: Bitmap, targetSize: Int): ImageBitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width == height) {
            return Bitmap.createScaledBitmap(
                bitmap,
                targetSize,
                targetSize,
                true
            ).asImageBitmap()
        } else if (width > height) {
            val xStart = (width - height) / 2
            val croppedImage = Bitmap.createBitmap(
                bitmap,
                xStart,
                0,
                height,
                height
            )
            return Bitmap.createScaledBitmap(
                croppedImage,
                targetSize,
                targetSize,
                true
            ).asImageBitmap()
        } else {
            val yStart = (height - width) / 2
            val croppedImage = Bitmap.createBitmap(
                bitmap,
                0,
                yStart,
                width,
                width
            )
            return Bitmap.createScaledBitmap(
                croppedImage,
                targetSize,
                targetSize,
                true
            ).asImageBitmap()
        }
    }
}