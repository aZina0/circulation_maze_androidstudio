package com.aZina0.circulationmaze

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.ImageBitmap
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


data class BoardSmallInfo(
    val gameData: GameData,
    val time: Int,
)

@Singleton
class BoardManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun saveBoard(
        gameData: GameData,
        imageBitmap: ImageBitmap,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        val user = Firebase.auth.currentUser
        if (user == null) {
            return
        }

        Toast.makeText(context, "board saved", Toast.LENGTH_LONG).show()
        val db = FirebaseFirestore.getInstance()
        db.collection("solvedBoards")
            .document(gameData.uid)
            .set(
                mapOf(
                    "gameData" to Global.gameDataToString(gameData),
                    "time" to 0,
                    "image" to Global.imageBitmapToByteString(imageBitmap),
                    "user" to user.uid,
                )
            )
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getAllSolvedBoards(
        userUid: String,
        accountManager: AccountManager,
        onSuccess: (boardSmallInfos: List<BoardSmallInfo>) -> Unit,
        onFailure: () -> Unit,
    ) {
        val boardSmallInfos = mutableListOf<BoardSmallInfo>()

        val db = FirebaseFirestore.getInstance()
        db.collection("solvedBoards")
            .whereEqualTo("user", userUid)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents.documents) {
                        val gameDataString = document.getString("gameData")!!
                        val gameData = Global.stringToGameData(gameDataString)

                        val time = document.getLong("time")!!.toInt()

                        boardSmallInfos.add(
                            BoardSmallInfo(
                                gameData = gameData,
                                time = time,
                            )
                        )
                    }
                    onSuccess(boardSmallInfos)

                } else {
                    onFailure()
                }
            }
            .addOnFailureListener {
                onFailure()
            }
    }
}