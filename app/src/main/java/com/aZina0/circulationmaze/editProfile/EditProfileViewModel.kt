package com.aZina0.circulationmaze.editProfile

import android.app.Application
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.AccountManager
import com.aZina0.circulationmaze.Global
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val accountManager: AccountManager,
    savedStateHandle: SavedStateHandle,
    private val application: Application
): ViewModel() {
    var userUid by mutableStateOf("")
        private set

    var image by mutableStateOf(accountManager.defaultProfileImage)
        private set
    var loadedImage by mutableStateOf<ImageBitmap?>(null)
        private set

    var username by mutableStateOf("")
        private set
    var newUsername by mutableStateOf("")
        private set
    var usernameError by mutableStateOf(false)
        private set
    var usernameErrorText by mutableStateOf("")
        private set

    var staticDescription by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var descriptionError by mutableStateOf(false)
        private set
    var descriptionErrorText by mutableStateOf("")
        private set

    private var actualEmail by mutableStateOf("")
    var censoredEmail by mutableStateOf("")
        private set
    var newEmail by mutableStateOf("")
        private set
    var emailError by mutableStateOf(false)
        private set
    var emailErrorText by mutableStateOf("")
        private set
    var emailPassword by mutableStateOf("")
        private set
    var emailPasswordError by mutableStateOf(false)
        private set
    var emailPasswordErrorText by mutableStateOf("")
        private set

    var newPassword by mutableStateOf("")
        private set
    var newPasswordAgain by mutableStateOf("")
        private set
    var newPasswordError by mutableStateOf(false)
        private set
    var newPasswordErrorText by mutableStateOf("")
        private set
    var currentPassword by mutableStateOf("")
        private set
    var currentPasswordError by mutableStateOf(false)
        private set
    var currentPasswordErrorText by mutableStateOf("")
        private set

    var loadingBarActive by mutableStateOf(false)
        private set


    init {
        userUid = savedStateHandle["userUid"] ?: ""
        refreshUi()
    }

    fun refreshUi() {
        loadingBarActive = true

        accountManager.getAccountInfo(
            userUid = userUid,
            onSuccess = { accountInfo ->
                username = accountInfo.username
                image = accountInfo.image
                description = accountInfo.description
                staticDescription = accountInfo.description

                actualEmail = accountInfo.email
                val fullEmail = accountInfo.email
                val emailSplit = fullEmail.split("@")
                val firstPart = emailSplit[0]
                val lastPart = emailSplit[1]
                var censoredFirstPart = ""
                if (firstPart.length >= 3) {
                    censoredFirstPart =
                        firstPart.first() +
                                "*".repeat(firstPart.length - 2) +
                                firstPart.last()
                } else {
                    censoredFirstPart = "*".repeat(firstPart.length)
                }
                censoredEmail = censoredFirstPart + "@" + lastPart
                loadingBarActive = false
            },
            onFailure = {

            }
        )
    }

    fun onOpenFiles() {

    }

    fun imageLoaded(imageUri: Uri) {
        val inputStream = application.applicationContext.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        loadedImage = Global.getCroppedScaledImageBitmap(
            bitmap = bitmap,
            targetSize = 300,
        )
    }

    fun onChangeImageClicked() {
        accountManager.updateImage(
            loadedImage!!,
            onSuccess = {
                accountManager.getImage(
                    onSuccess = {
                        image = it
                        loadedImage = null
                    },
                    onFailure = {

                    }
                )
            },
            onFailure = {

            }
        )
    }

    fun onUsernameChange(newValue: String) {
        newUsername = newValue
        usernameError = false
        usernameErrorText = ""
    }

    fun onUpdateUsernameClicked() {
        if (newUsername.length <= 2) {
            usernameError = true
            usernameErrorText = "Username must be longer than 2 characters."
            return
        }

        accountManager.updateUsername(
            newUsername = newUsername,
            onSuccess = {
                newUsername = ""
                refreshUi()
            },
            onFailure = {
                usernameError = true
                usernameErrorText = "Couldn't update username."
            }
        )
    }

    fun onDescriptionChange(newValue: String) {
        description = newValue
        descriptionError = false
        descriptionErrorText = ""
    }

    fun onUpdateDescriptionClicked() {
        accountManager.updateDescription(
            newDescription = description,
            onSuccess = {
                refreshUi()
            },
            onFailure = {
                descriptionError = true
                descriptionErrorText = "Couldn't update 'about'."
            }
        )
    }

    fun onEmailChange(newValue: String) {
        newEmail = newValue
        emailError = false
        emailErrorText = ""
    }

    fun onEmailPasswordChange(newValue: String) {
        emailPassword = newValue
        emailPasswordError = false
        emailPasswordErrorText = ""
    }

    fun onUpdateEmailClicked() {
        if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            emailError = true
            emailErrorText = "Invalid email."
            return
        }

        val user = Firebase.auth.currentUser ?: return
        val credential = EmailAuthProvider.getCredential(actualEmail, emailPassword)

        user.reauthenticate(credential)
            .addOnSuccessListener {
                user.verifyBeforeUpdateEmail(newEmail)
                    .addOnSuccessListener {
                        emailErrorText = "You will receive a confirmation email on $newEmail. Follow the link inside to verify the new email address."
                        newEmail = ""
                        emailPassword = ""
                        refreshUi()
                    }
                    .addOnFailureListener {
                        emailError = true
                        emailErrorText = "Could not update email."
                    }
            }
            .addOnFailureListener {
                emailError = true
                emailErrorText = "Could not authenticate."
            }
    }


    fun onNewPasswordChange(newValue: String) {
        newPassword = newValue
        newPasswordError = false
        newPasswordErrorText = ""
    }

    fun onNewPasswordAgainChange(newValue: String) {
        newPasswordAgain = newValue
        newPasswordError = false
        newPasswordErrorText = ""
    }

    fun onCurrentPasswordChange(newValue: String) {
        currentPassword = newValue
        currentPasswordError = false
        currentPasswordErrorText = ""
    }

    fun onUpdatePasswordClicked() {
        if (newPassword != newPasswordAgain) {
            newPasswordError = true
            newPasswordErrorText = "Passwords must match."
            return
        }
        if (newPassword.length < 8) {
            newPasswordError = true
            newPasswordErrorText = "Password must be longer than 7 characters."
            return
        }
        if (!newPassword.contains(Regex("\\d"))) {
            newPasswordError = true
            newPasswordErrorText = "Password must contain at least one digit."
            return
        }

        val user = Firebase.auth.currentUser ?: return
        val credential = EmailAuthProvider.getCredential(actualEmail, currentPassword)

        user.reauthenticate(credential)
            .addOnSuccessListener {
                user.updatePassword(newPassword)
                    .addOnSuccessListener {
                        currentPassword = ""
                        newPassword = ""
                        newPasswordAgain = ""
                        refreshUi()
                    }
                    .addOnFailureListener {
                        currentPasswordError = true
                        currentPasswordErrorText = "Could not update password."
                    }
            }
            .addOnFailureListener {
                currentPasswordError = true
                currentPasswordErrorText = "Could not authenticate."
            }

    }
}