package com.aZina0.circulationmaze

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileManager @Inject constructor(
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
}