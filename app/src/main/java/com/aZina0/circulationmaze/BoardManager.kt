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

data class BoardInfo(
    val gameData: GameData,
    val time: Int,
    val imageBitmap: ImageBitmap,
    val userUid: String,
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
                    "gridSize" to gameData.gridSize,
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

    fun shareBoard(
        boardUid: String,
        accountManager: AccountManager,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        if (!accountManager.isLoggedIn() || !accountManager.isOnline()) {
            onFailure()
            return
        }

        val user = Firebase.auth.currentUser!!
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(user.uid)
            .collection("sharedBoards")
            .document(boardUid)
            .set(emptyMap<String, Any>())
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getBoardInfo(
        boardUid: String,
        accountManager: AccountManager,
        onSuccess: (boardInfo: BoardInfo) -> Unit,
        onFailure: () -> Unit,
    ) {
        if (!accountManager.isOnline()) {
            onFailure()
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("solvedBoards")
            .document(boardUid)
            .get()
            .addOnSuccessListener { document ->
                val gameDataString = document.getString("gameData")!!
                val gameData = Global.stringToGameData(gameDataString)

                val imageBitmapString = document.getString("image")!!
                val imageBitmap = Global.byteStringToImageBitmap(imageBitmapString)

                val time = document.getLong("time")!!.toInt()

                val userUid = document.getString("user")!!

                onSuccess(
                    BoardInfo(
                        gameData = gameData,
                        time = time,
                        imageBitmap = imageBitmap,
                        userUid = userUid
                    )
                )
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getSharedBoards(
        userUid: String,
        accountManager: AccountManager,
        onSuccess: (boardSmallInfos: List<BoardSmallInfo>) -> Unit,
        onFailure: () -> Unit,
    ) {
        if (!accountManager.isOnline()) {
            onFailure()
            return
        }

        val boardSmallInfos = mutableListOf<BoardSmallInfo>()

        var requestsSent = 0
        var responsesReceived = 0
        val checkForAllResponses: () -> Unit = {
            if (responsesReceived >= requestsSent) {
                onSuccess(boardSmallInfos)
            }
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userUid)
            .collection("sharedBoards")
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents.documents) {
                    val boardUid = document.id

                    requestsSent++
                    getBoardInfo(
                        boardUid = boardUid,
                        accountManager = accountManager,
                        onSuccess = { boardInfo ->
                            boardSmallInfos.add(
                                BoardSmallInfo(
                                    gameData = boardInfo.gameData,
                                    time = boardInfo.time,
                                )
                            )
                            responsesReceived++
                            checkForAllResponses()
                        },
                        onFailure = {
                            responsesReceived++
                            checkForAllResponses()
                        }
                    )
                }
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getBestBoardInfos(
        gridSize: Int,
        accountManager: AccountManager,
        onSuccess: (bestBoardInfos: List<BoardInfo>) -> Unit,
        onFailure: () -> Unit,
    ) {
        val bestBoardInfos = mutableListOf<BoardInfo>()

        val db = FirebaseFirestore.getInstance()
        db.collection("solvedBoards")
            .whereEqualTo("gridSize", gridSize)
            .orderBy("time")
            .limit(100)
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents.documents) {
                    val gameDataString = document.getString("gameData")!!
                    val gameData = Global.stringToGameData(gameDataString)

                    val imageBitmapString = document.getString("image")!!
                    val imageBitmap = Global.byteStringToImageBitmap(imageBitmapString)

                    val time = document.getLong("time")!!.toInt()

                    val userUid = document.getString("user")!!

                    bestBoardInfos.add(
                        BoardInfo(
                            gameData = gameData,
                            time = time,
                            imageBitmap = imageBitmap,
                            userUid = userUid
                        )
                    )
                }

                onSuccess(bestBoardInfos)

            }
            .addOnFailureListener { e ->
                onFailure()
                Global.print(e.toString())
            }
    }
}