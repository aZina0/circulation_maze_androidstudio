package com.aZina0.circulationmaze

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
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

    fun checkIfUsernameExists(username: String): Boolean {
        return true
    }

    fun getUsername(
        onSuccess: (username: String) -> Unit,
        onFailure: () -> Unit,
    ) {
        val user = Firebase.auth.currentUser
        if (user != null) {

            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    onSuccess(document.data?.get("username").toString())
                }
                .addOnFailureListener {
                    onFailure()
                }
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