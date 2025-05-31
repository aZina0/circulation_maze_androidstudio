package com.aZina0.circulationmaze

import android.content.Context
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
                            "username" to username
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