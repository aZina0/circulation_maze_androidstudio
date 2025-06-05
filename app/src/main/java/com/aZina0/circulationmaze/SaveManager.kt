package com.aZina0.circulationmaze

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

data class GameData(
    val uid: String,
    val seed: Long,
    val gridSize: Int,
    val lastModifiedDate: LocalDateTime,
    var savedOnCloud: Boolean,
    val pieces: JsonObject,
)

data class SaveInfo(
    val gameData: GameData,
    val imageBitmap: ImageBitmap?,
)

@Singleton
class SaveManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun syncSavesAndReturn(
        accountManager: AccountManager,
        onFinished: (saveList: List<SaveInfo>) -> Unit,
    ) {
        val localSaves = mutableMapOf<String, SaveInfo>()

        val dir = File(context.filesDir, "saves")
        if (dir.exists() && dir.isDirectory) {
            for (saveUid in dir.list()!!) {
                val gameData = loadGameFromFile(saveUid)!!
                localSaves[saveUid] = SaveInfo(
                    gameData = gameData,
                    imageBitmap = loadGameImageFromFile(gameData)
                )
            }
        }

        if (!accountManager.isOnline() || !accountManager.isLoggedIn()) {
            onFinished(localSaves.values.toList())
            return
        }


        var requestsSent = 0
        var responsesReceived = 0
        val cloudSaves = mutableMapOf<String, SaveInfo>()

        val checkForAllResponses: () -> Unit = {
            if (responsesReceived >= requestsSent) {
                val saveList = mutableListOf<SaveInfo>()

                var cloudSaveUids = cloudSaves.keys
                for (cloudSaveUid in cloudSaveUids) {
                    if (cloudSaveUid in localSaves) {
                        val cloudSaveInfo = cloudSaves[cloudSaveUid]!!
                        val localSaveInfo = localSaves[cloudSaveUid]!!

                        val cloudSaveDate = cloudSaveInfo.gameData.lastModifiedDate
                        val localSaveDate = localSaveInfo.gameData.lastModifiedDate

                        if (cloudSaveDate == localSaveDate) {
                            localSaves.remove(cloudSaveUid)
                        } else if (cloudSaveDate.isAfter(localSaveDate)) {
                            localSaves.remove(cloudSaveUid)
                            saveGameToFile(cloudSaveInfo.gameData)
                            saveGameImageToFile(cloudSaveInfo.gameData, cloudSaveInfo.imageBitmap!!)
                        } else if (localSaveDate.isAfter(cloudSaveDate)) {
                            cloudSaves.remove(cloudSaveUid)
                            saveGameToCloud(localSaveInfo.gameData)
                            saveGameImageToCloud(localSaveInfo.gameData, localSaveInfo.imageBitmap!!)
                        }
                    }
                }

                val localSaveUids = localSaves.keys
                for (localSaveUid in localSaveUids) {
                    if (localSaveUid !in cloudSaves) {
                        val localSaveInfo = localSaves[localSaveUid]!!

                        localSaveInfo.gameData.savedOnCloud = true
                        saveGameToCloud(localSaveInfo.gameData)
                        saveGameImageToCloud(localSaveInfo.gameData, localSaveInfo.imageBitmap!!)
                    }
                }

                cloudSaveUids = cloudSaves.keys
                for (cloudSaveUid in cloudSaveUids) {
                    if (cloudSaveUid !in localSaves) {
                        val cloudSaveInfo = cloudSaves[cloudSaveUid]!!

                        saveGameToFile(cloudSaveInfo.gameData)
                        saveGameImageToFile(cloudSaveInfo.gameData, cloudSaveInfo.imageBitmap!!)
                    }
                }

                saveList.addAll(localSaves.values)
                saveList.addAll(cloudSaves.values)
                onFinished(saveList)
            }
        }

        val user = Firebase.auth.currentUser!!
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(user.uid)
            .collection("saves")
            .get()
            .addOnSuccessListener { task ->
                for (document in task.documents) {
                    requestsSent++
                    val saveUid = document.id
                    loadGameAndImageFromCloud(
                        uid = saveUid,
                        onSuccess = { saveInfo ->
                            saveInfo!!.gameData.savedOnCloud = true
                            cloudSaves[saveUid] = saveInfo
                            responsesReceived++
                            checkForAllResponses()
                        },
                        onFailure = {
                            responsesReceived++
                            checkForAllResponses()
                        }
                    )
                }
                checkForAllResponses()
            }
    }

    fun saveGame(gameData: GameData) {
        saveGameToFile(gameData)

        if (Firebase.auth.currentUser != null) {
            saveGameToCloud(gameData)
        }
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

    private fun saveGameToFile(gameData: GameData) {
        val dir = File(context.filesDir, "saves")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, gameData.uid)
        file.writeText(gameDataToString(gameData))
    }

    private fun saveGameToCloud(gameData: GameData) {
        val user = Firebase.auth.currentUser
        if (user == null) {
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(user.uid)
            .collection("saves")
            .document(gameData.uid)
            .set(mapOf("data" to gameDataToString(gameData)))
//            .addOnSuccessListener {
//                gameData.savedOnCloud = true
//                saveGameToFile(gameData)
//            }
    }

    fun saveGameImage(gameData: GameData, imageBitmap: ImageBitmap) {
        saveGameImageToFile(gameData, imageBitmap)
        saveGameImageToCloud(gameData, imageBitmap)
    }

    fun saveGameImageToFile(gameData: GameData, imageBitmap: ImageBitmap) {
        val bitmap = imageBitmap.asAndroidBitmap()
        val byteStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream)
        val bytes = byteStream.toByteArray()

        val dir = File(context.filesDir, "save_images")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, gameData.uid)
        file.writeBytes(bytes)
    }

    fun saveGameImageToCloud(gameData: GameData, imageBitmap: ImageBitmap) {
        val bitmap = imageBitmap.asAndroidBitmap()
        val byteStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream)
        val bytes = byteStream.toByteArray()

        val user = Firebase.auth.currentUser
        if (user == null) {
            return
        }

        val base64String = Base64.encodeToString(bytes, Base64.DEFAULT)
        val data = mapOf("data" to base64String)

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(user.uid)
            .collection("save_images")
            .document(gameData.uid)
            .set(data)
    }



    fun loadGameFromFile(uid: String): GameData? {
        val file = File(File(context.filesDir, "saves"), uid)
        if (file.exists()) {
            return stringToGameData(file.readText())
        } else {
            return null
        }
    }

    fun loadGameImageFromFile(gameData: GameData): ImageBitmap? {
        val saveImagesDir = File(context.filesDir, "save_images")
        val imageFile = File(saveImagesDir, gameData.uid)

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

    fun loadGameAndImageFromCloud(
        uid: String,
        onSuccess: (saveInfo: SaveInfo?) -> Unit,
        onFailure: () -> Unit,
    ) {
        val user = Firebase.auth.currentUser
        if (user == null) {
            return
        }

        var gameData: GameData? = null
        var imageBitmap: ImageBitmap? = null

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(user.uid)
            .collection("saves")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                gameData = stringToGameData(document.getString("data")!!)

                if (imageBitmap != null) {
                    onSuccess(
                        SaveInfo(
                            gameData = gameData!!,
                            imageBitmap = imageBitmap,
                        )
                    )
                }
            }
            .addOnFailureListener {
                onFailure()
            }

        db.collection("users")
            .document(user.uid)
            .collection("save_images")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                val imageBytes = Base64.decode(document.getString("data"), Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imageBitmap = bitmap.asImageBitmap()

                if (gameData != null) {
                    onSuccess(
                        SaveInfo(
                            gameData = gameData!!,
                            imageBitmap = imageBitmap,
                        )
                    )
                }
            }
            .addOnFailureListener {
                onFailure()
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

    fun deleteAllLocalSaves() {
        val dir = File(context.filesDir, "saves")
        if (dir.exists() && dir.isDirectory) {
            for (fileName in dir.list()!!) {
                deleteSaveGameFromFile(fileName)
            }
        }
    }

    fun deleteSaveGameFromFile(uid: String) {
        val savesDir = File(context.filesDir, "saves")
        if (savesDir.exists() && savesDir.isDirectory) {
            for (fileName in savesDir.list()!!) {
                if (fileName == uid) {
                    val saveFile = File(savesDir, uid)
                    if (saveFile.isFile) {
                        saveFile.delete()
                    }
                }
            }
        }

        val saveImagesDir = File(context.filesDir, "save_images")
        if (saveImagesDir.exists() && saveImagesDir.isDirectory) {
            for (fileName in saveImagesDir.list()!!) {
                if (fileName == uid) {
                    val imageFile = File(saveImagesDir, uid)
                    if (imageFile.isFile) {
                        imageFile.delete()
                    }
                }
            }
        }
    }

    fun deleteSaveGameFromCloud(
        uid: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        val user = Firebase.auth.currentUser!!
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(user.uid)
            .collection("saves")
            .document(uid)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }

        db.collection("users")
            .document(user.uid)
            .collection("save_images")
            .document(uid)
            .delete()
    }

    private fun gameDataToString(gameData: GameData): String {
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

    private fun stringToGameData(string: String): GameData {
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
}