package com.aZina0.circulationmaze

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
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

    fun readGameFromFile(uid: String): String {
        val file = File(File(context.filesDir, "saves"), uid)

        if (file.exists()) {
            return file.readText()
        } else {
            return ""
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
}