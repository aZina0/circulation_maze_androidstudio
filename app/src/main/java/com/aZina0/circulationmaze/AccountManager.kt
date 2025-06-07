package com.aZina0.circulationmaze

import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


data class AccountInfo(
    val userUid: String,
    val username: String,
    val email: String,
    val xp: Int,
    val image: ImageBitmap,
    val description: String,
)

@Singleton
class AccountManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val defaultProfileImage = BitmapFactory.decodeResource(
        context.resources, R.drawable.default_picture
    ).asImageBitmap()

    fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }

    fun isLoggedIn(): Boolean {
        return Firebase.auth.currentUser != null
    }

    fun followUser(
        userUidFollowing: String,
        userUidFollowed: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val task1 = db
            .collection("users")
            .document(userUidFollowing)
            .collection("followedUsers")
            .document(userUidFollowed)
            .set(emptyMap<String, Any>())
        val task2 = db
            .collection("users")
            .document(userUidFollowed)
            .collection("followingUsers")
            .document(userUidFollowing)
            .set(emptyMap<String, Any>())

        Tasks.whenAllSuccess<QuerySnapshot>(task1, task2)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun unfollowUser(
        userUidFollowing: String,
        userUidFollowed: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val task1 = db
            .collection("users")
            .document(userUidFollowing)
            .collection("followedUsers")
            .document(userUidFollowed)
            .delete()
        val task2 = db
            .collection("users")
            .document(userUidFollowed)
            .collection("followingUsers")
            .document(userUidFollowing)
            .delete()

        Tasks.whenAllSuccess<QuerySnapshot>(task1, task2)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun checkIfFollowed(
        userUidFollowing: String,
        userUidFollowed: String,
        onSuccess: (followed: Boolean) -> Unit,
        onFailure: () -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userUidFollowing)
            .collection("followedUsers")
            .document(userUidFollowed)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.exists()) {
                    onSuccess(true)
                } else {
                    onSuccess(false)
                }
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getFollowers(
        userUid: String,
        onSuccess: (followerList: List<AccountInfo>) -> Unit,
        onFailure: () -> Unit
    ) {
        val followerList = mutableListOf<AccountInfo>()

        var requestsSent = 0
        var responsesReceived = 0
        val checkForAllResponses: () -> Unit = {
            if (responsesReceived >= requestsSent) {
                onSuccess(followerList)
            }
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userUid)
            .collection("followingUsers")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents.documents) {
                    requestsSent++
                    getAccountInfo(
                        userUid = document.id,
                        onSuccess = {
                            followerList.add(it)
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
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getFollowing(
        userUid: String,
        onSuccess: (followingList: List<AccountInfo>) -> Unit,
        onFailure: () -> Unit
    ) {
        val followingList = mutableListOf<AccountInfo>()

        var requestsSent = 0
        var responsesReceived = 0
        val checkForAllResponses: () -> Unit = {
            if (responsesReceived >= requestsSent) {
                onSuccess(followingList)
            }
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userUid)
            .collection("followedUsers")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents.documents) {
                    requestsSent++
                    getAccountInfo(
                        userUid = document.id,
                        onSuccess = {
                            followingList.add(it)
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
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getAccountInfo(
        userUid: String,
        onSuccess: (accountInfo: AccountInfo) -> Unit,
        onFailure: () -> Unit
    ) {

        if (!isOnline()) {
            onFailure()
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userUid)
            .get()
            .addOnSuccessListener { document ->
                val username = document.data?.get("username").toString()
                val email = document.data?.get("email").toString()
                val xp = document.data?.get("xp").toString().toInt()
                val image = Global.byteStringToImageBitmap(
                    document.data?.get("image").toString()
                )
                val description = document.data?.get("description").toString()
                onSuccess(
                    AccountInfo(
                        userUid = userUid,
                        username = username,
                        email = email,
                        xp = xp,
                        image = image,
                        description = description
                    )
                )
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getImage(
        onSuccess: (imageBitmap: ImageBitmap) -> Unit,
        onFailure: () -> Unit,
    ) {
        if (!isOnline() || !isLoggedIn()) {
            return
        }

        val user = Firebase.auth.currentUser!!
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                onSuccess(
                    Global.byteStringToImageBitmap(
                        document.data?.get("image").toString())
                    )
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getUsername(
        userUid: String,
        onSuccess: (username: String) -> Unit,
        onFailure: () -> Unit,
    ) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userUid)
            .get()
            .addOnSuccessListener { document ->
                onSuccess(document.data?.get("username").toString())
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getDescription(
        userUid: String,
        onSuccess: (description: String) -> Unit,
        onFailure: () -> Unit,
    ) {
        if (!isOnline()) {
            onFailure()
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userUid)
            .get()
            .addOnSuccessListener { document ->
                onSuccess(document.data?.get("description").toString())
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getXp(
        onSuccess: (xp: Int) -> Unit,
        onFailure: () -> Unit,
    ) {
        val user = Firebase.auth.currentUser
        if (user != null) {

            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    onSuccess(document.data?.get("xp").toString().toInt())
                }
                .addOnFailureListener {
                    onFailure()
                }
        }
    }

    fun getLevelAndRemainder(xp: Int): Pair<Int, Float> {
        val level = xp.floorDiv(100)
        val xpRemainder = (xp - level * 100) / 100f
        return Pair(level, xpRemainder)
    }

    fun updateImage(
        newImageBitmap: ImageBitmap,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val user = Firebase.auth.currentUser!!
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(user.uid)
            .update("image", Global.imageBitmapToByteString(newImageBitmap))
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun updateUsername(
        newUsername: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {

        val user = Firebase.auth.currentUser!!
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(user.uid)
            .update("username", newUsername)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun updateDescription(
        newDescription: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {

        val user = Firebase.auth.currentUser!!
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(user.uid)
            .update("description", newDescription)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun attemptRegister(
        username: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onUsernameFail: () -> Unit,
        onAccountExistsFail: () -> Unit,
    ) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val user = Firebase.auth.currentUser
                    if (user == null) {
                        onUsernameFail()
                        deleteAccount()
                    } else {
                        val initialData = mapOf(
                            "username" to username,
                            "email" to email,
                            "xp" to 0,
                            "image" to Global.imageBitmapToByteString(
                                defaultProfileImage
                            ),
                            "description" to "",
                        )

                        val db = FirebaseFirestore.getInstance()
                        db.collection("users")
                            .document(user.uid)
                            .set(initialData)
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener {
                                onUsernameFail()
                                deleteAccount()
                            }

                    }

                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        onAccountExistsFail()
                    }
                }
            }
    }

    fun attemptLogin(
        usernameOrEmail: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
        onUsernameFailure: (message: String) -> Unit,
    ) {
        if (usernameOrEmail.contains("@")) {
            Firebase.auth.signInWithEmailAndPassword(usernameOrEmail, password)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure()
                }
        } else {
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .whereEqualTo("username", usernameOrEmail)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0]
                        val email = document.getString("email")
                        if (email.isNullOrEmpty()) {
                            onUsernameFailure("Username does not exist.")
                        } else {
                            Firebase.auth.signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener {
                                    onSuccess()
                                }
                                .addOnFailureListener {
                                    onFailure()
                                }
                        }
                    } else {
                        onUsernameFailure("Username does not exist.")
                    }
                }
                .addOnFailureListener {
                    onUsernameFailure("Username does not exist.")
                }
        }
    }

    fun signOut(saveManager: SaveManager) {
        Firebase.auth.signOut()
        saveManager.deleteAllLocalSaves()
    }

    private fun deleteAccount() {
        val user = Firebase.auth.currentUser
        if (user == null) {
            return
        }

        val uid = user.uid

        user.delete()
//            .addOnFailureListener { exception ->
//                Toast.makeText(context, exception.toString(), Toast.LENGTH_LONG).show()
//            }

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(uid)
            .delete()
//            .addOnFailureListener { exception ->
//                Toast.makeText(context, exception.toString(), Toast.LENGTH_LONG).show()
//            }

    }
}