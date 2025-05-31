package com.aZina0.circulationmaze

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import javax.inject.Inject
import javax.inject.Singleton

data class GameBasicInfo(val uid: String, val seed: Long, val gridSize: Int, val lastModifiedDate: String)

@Singleton
class SaveManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getBasicInfo(jsonObject: JsonObject): GameBasicInfo {
        val uid = jsonObject["uid"]!!.jsonPrimitive.content
        val seed = jsonObject["seed"]!!.jsonPrimitive.long
        val gridSize = jsonObject["gridSize"]!!.jsonPrimitive.int
        val lastModifiedDate = jsonObject["lastModifiedDate"]!!.jsonPrimitive.content
        return GameBasicInfo(uid, seed, gridSize, lastModifiedDate)
    }
}