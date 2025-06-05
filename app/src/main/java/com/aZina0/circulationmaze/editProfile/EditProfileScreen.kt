package com.aZina0.circulationmaze.editProfile

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aZina0.circulationmaze.CustomHeader
import com.aZina0.circulationmaze.Global

@Composable
fun EditProfileScreen(
    onReturnClicked: (userUid: String) -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    val padding = Global.relativeWidth(0.03f)

    val pickPictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { imageUri ->
        if (imageUri != null) {
            viewModel.imageLoaded(imageUri)
        }
    }

    Column {
        CustomHeader("Edit profile", viewModel.loadingBarActive)

        Column {
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
                        text = "Profile picture"
                    )
                    Row {
                        Image(
                            bitmap = viewModel.image,
                            contentDescription = "picture",
                            modifier = Modifier
                                .width(Global.relativeWidth(.4f))
                                .height(Global.relativeWidth(.4f)),
                            contentScale = ContentScale.Crop
                        )
                        Column {
                            Button(
                                onClick = { pickPictureLauncher.launch("image/*") },
                            ) {}
                            if (viewModel.loadedImage != null) {
                                Image(
                                    bitmap = viewModel.loadedImage!!,
                                    contentDescription = "picture",
                                    modifier = Modifier
                                        .width(Global.relativeWidth(.2f))
                                        .height(Global.relativeWidth(.2f)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Button(
                                onClick = { viewModel.onChangeImageClicked() },
                                enabled = viewModel.loadedImage != null
                            ) {}
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