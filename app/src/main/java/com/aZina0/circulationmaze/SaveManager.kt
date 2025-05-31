package com.aZina0.circulationmaze

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

data class GameBasicInfo(
    val uid: String,
    val seed: Long,
    val gridSize: Int,
    val lastModifiedDate: String
)

@Singleton
class SaveManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun saveGame(newBasicInfo: GameBasicInfo, jsonString: String) {
        saveGameToFile(newBasicInfo, jsonString)

//        val existingJsonObject = readJsonObjectFromFile(newBasicInfo.uid)
//        val existingBasicInfo = getBasicInfo(existingJsonObject!!)
//
//        val existingDate = LocalDateTime.parse(
//            existingBasicInfo.lastModifiedDate,
//            DateTimeFormatter.ISO_DATE_TIME
//        )
//        val newDate = LocalDateTime.parse(
//            newBasicInfo.lastModifiedDate,
//            DateTimeFormatter.ISO_DATE_TIME
//        )
//
//        if (existingDate.isBefore(newDate)) {
//
//        }
    }

    private fun saveGameToFile(basicInfo: GameBasicInfo, jsonString: String) {
        val dir = File(context.filesDir, "saves")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, basicInfo.uid)
        file.writeText(jsonString)
    }

    fun saveGameImage(basicInfo: GameBasicInfo, imageBitmap: ImageBitmap) {
        val bitmap = imageBitmap.asAndroidBitmap()
        val byteStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream)
        val bytes = byteStream.toByteArray()
        saveGameImageToFile(basicInfo, bytes)
    }

    fun saveGameImageToFile(basicInfo: GameBasicInfo, bytes: ByteArray) {
        val dir = File(context.filesDir, "save_images")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, basicInfo.uid)
        file.writeBytes(bytes)
    }

    fun loadGameImageFromFile(basicInfo: GameBasicInfo): ImageBitmap? {
        val saveImagesDir = File(context.filesDir, "save_images")
        val imageFile = File(saveImagesDir, basicInfo.uid)

        if (!imageFile.exists()) {
            return null
        }

        try {
            val bytes = imageFile.readBytes()
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            return bitmap?.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun readJsonObjectFromFile(uid: String): JsonObject? {
        val file = File(File(context.filesDir, "saves"), uid)
        if (file.exists()) {
            return Json.parseToJsonElement(file.readText()).jsonObject
        } else {
            return null
        }
    }

    fun getSavesList(): List<String> {
        val dir = File(context.filesDir, "saves")
        if (dir.exists() && dir.isDirectory) {
            return dir.list()!!.toList()
        } else {
            return emptyList()
        }
    }

    fun deleteAllSaves() {
        val dir = File(context.filesDir, "saves")
        if (dir.exists() && dir.isDirectory) {
            for (file in dir.listFiles()!!) {
                if (file.isFile) {
                    file.delete()
                }
            }
        }
    }

    fun getBasicInfo(jsonObject: JsonObject): GameBasicInfo {
        val uid = jsonObject["uid"]!!.jsonPrimitive.content
        val seed = jsonObject["seed"]!!.jsonPrimitive.long
        val gridSize = jsonObject["gridSize"]!!.jsonPrimitive.int
        val lastModifiedDate = jsonObject["lastModifiedDate"]!!.jsonPrimitive.content
        return GameBasicInfo(uid, seed, gridSize, lastModifiedDate)
    }
}