package com.aZina0.circulationmaze.editProfile

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global
import com.aZina0.circulationmaze.RelativeVerticalSpacer

@Composable
fun EditProfileScreen(
    onReturnClicked: (userUid: String) -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    val padding = Global.relativeWidth(0.03f)
    val scrollState = rememberScrollState()

    val pickPictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { imageUri ->
        if (imageUri != null) {
            viewModel.imageLoaded(imageUri)
        }
    }


    Column {
        CustomHeader(
            displayProgressBar = viewModel.loadingBarActive,
            middleComposable = {
                Text (
                    text = "Edit profile",
                    fontSize = Global.relativeFont(0.04f),
                    textAlign = TextAlign.Center
                )
            }
        )

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface (
                modifier = Modifier
                    .width(Global.relativeWidth(.9f)),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(padding)
                ) {
                    Text(
                        text = "Image",
                        fontSize = Global.relativeFont(0.025f)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Image(
                            bitmap = viewModel.image,
                            contentDescription = "picture",
                            modifier = Modifier
                                .size(Global.relativeWidth(0.35f)),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            if (viewModel.loadedImage != null) {
                                Image(
                                    bitmap = viewModel.loadedImage!!,
                                    contentDescription = "picture",
                                    modifier = Modifier
                                        .size(Global.relativeWidth(0.17f)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(Global.relativeWidth(0.17f))
                                ) {}
                            }

                            Button(
                                onClick = { pickPictureLauncher.launch("image/*") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                ),
                            ) {
                                Text(
                                    text = "Load image",
                                    fontSize = Global.relativeFont(0.015f),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        textDecoration = TextDecoration.Underline,
                                    ),
                                )
                            }
                            Button(
                                onClick = { viewModel.onChangeImageClicked() },
                                enabled = viewModel.loadedImage != null,
                            ) {
                                Text(
                                    text = "Save",
                                    fontSize = Global.relativeFont(0.02f)
                                )
                            }
                        }
                    }
                }
            }
            RelativeVerticalSpacer(.02f)
            Surface (
                modifier = Modifier
                    .width(Global.relativeWidth(.9f)),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(padding)
                ) {
                    Row {
                        Text(
                            text = "Username",
                            fontSize = Global.relativeFont(0.025f),
                        )
                        Text(
                            text = viewModel.username,
                            modifier = Modifier
                                .weight(1f),
                            fontSize = Global.relativeFont(0.025f),
                            textAlign = TextAlign.End
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.newUsername,
                        onValueChange = { viewModel.onUsernameChange(it) },
                        label = { Text(text = "new username") },
                        singleLine = true,
                        isError = viewModel.usernameError,
                        supportingText = { Text(text = viewModel.usernameErrorText) },
                    )
                    Button(
                        onClick = { viewModel.onUpdateUsernameClicked() },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        enabled = viewModel.newUsername != ""
                    ) {
                        Text(
                            text = "Save",
                            fontSize = Global.relativeFont(0.02f)
                        )
                    }
                }
            }
            RelativeVerticalSpacer(.02f)
            Surface (
                modifier = Modifier
                    .width(Global.relativeWidth(.9f)),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(padding)
                ) {
                    Text(
                        text = "About",
                        fontSize = Global.relativeFont(0.025f)
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.description,
                        onValueChange = { viewModel.onDescriptionChange(it) },
                        label = { },
                        isError = viewModel.descriptionError,
                        supportingText = { Text(text = viewModel.descriptionErrorText) },
                    )
                    Button(
                        onClick = { viewModel.onUpdateDescriptionClicked() },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        enabled = viewModel.description != viewModel.staticDescription
                    ) {
                        Text(
                            text = "Save",
                            fontSize = Global.relativeFont(0.02f)
                        )
                    }
                }
            }
            RelativeVerticalSpacer(.02f)
            Surface (
                modifier = Modifier
                    .width(Global.relativeWidth(.9f)),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(padding)
                ) {
                    Row {
                        Text(
                            text = "Email",
                            fontSize = Global.relativeFont(0.025f)
                        )
                        Text(
                            text = viewModel.email,
                            fontSize = Global.relativeFont(0.02f),
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .weight(1f),
                        )
                    }
                    Column {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = viewModel.newEmail,
                            onValueChange = { viewModel.onEmailChange(it) },
                            label = { Text(text = "new email") },
                            singleLine = true,
                            isError = viewModel.emailError,
                            supportingText = { Text(text = viewModel.emailErrorText) },
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = viewModel.emailPassword,
                            onValueChange = { viewModel.onEmailPasswordChange(it) },
                            visualTransformation = PasswordVisualTransformation(),
                            label = { Text(text = "current password") },
                            singleLine = true,
                            isError = viewModel.emailPasswordError,
                            supportingText = { Text(text = viewModel.emailPasswordErrorText) },
                        )
                        Button(
                            onClick = { viewModel.onUpdateEmailClicked() },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            enabled =
                                viewModel.newEmail != "" &&
                                viewModel.emailPassword != ""
                        ) {
                            Text(
                                text = "Save",
                                fontSize = Global.relativeFont(0.02f)
                            )
                        }
                    }
                }
            }
            RelativeVerticalSpacer(.02f)
            Surface (
                modifier = Modifier
                    .width(Global.relativeWidth(.9f)),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(padding)
                ) {
                    Text(
                        text = "Password",
                        fontSize = Global.relativeFont(0.025f)
                    )
                    Column {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = viewModel.newPassword,
                            onValueChange = { viewModel.onNewPasswordChange(it) },
                            visualTransformation = PasswordVisualTransformation(),
                            label = { Text(text = "new password") },
                            singleLine = true,
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = viewModel.newPasswordAgain,
                            onValueChange = { viewModel.onNewPasswordAgainChange(it) },
                            visualTransformation = PasswordVisualTransformation(),
                            label = { Text(text = "repeat new password") },
                            singleLine = true,
                            isError = viewModel.newPasswordError,
                            supportingText = { Text(text = viewModel.newPasswordErrorText) },
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = viewModel.currentPassword,
                            onValueChange = { viewModel.onCurrentPasswordChange(it) },
                            visualTransformation = PasswordVisualTransformation(),
                            label = { Text(text = "current password") },
                            singleLine = true,
                            isError = viewModel.currentPasswordError,
                            supportingText = { Text(text = viewModel.currentPasswordErrorText) },
                        )
                        Button(
                            onClick = { viewModel.onUpdatePasswordClicked() },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            enabled =
                                viewModel.newPassword != "" &&
                                viewModel.newPasswordAgain != "" &&
                                viewModel.currentPassword != ""
                        ) {
                            Text(
                                text = "Save",
                                fontSize = Global.relativeFont(0.02f),
                            )
                        }
                    }
                }
            }
        }
    }

    BackHandler(enabled = true) {
        onReturnClicked(viewModel.userUid)
    }
}