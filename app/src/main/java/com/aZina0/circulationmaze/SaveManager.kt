package com.aZina0.circulationmaze

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

data class GameBasicInfo(val uid: String, val seed: Long, val gridSize: Int, val lastModifiedDate: String)

@Singleton
class SaveManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun saveGameToFile(uid: String, jsonString: String) {
        val dir = File(context.filesDir, "saves")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, uid)
        file.writeText(jsonString)
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