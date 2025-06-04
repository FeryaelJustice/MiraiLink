package com.feryaeljustice.mirailink.ui.screens.photo

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun ProfilePictureScreen(
    viewModel: ProfilePictureViewModel,
    sessionViewModel: GlobalSessionViewModel,
    onProfileUploaded: () -> Unit
) {
    val userId = sessionViewModel.currentUserId.collectAsState().value
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.uploadImage(it) }
    }
    val uploadResult by viewModel.uploadResult.collectAsState()

    LaunchedEffect(Unit) {
        sessionViewModel.showBars()
        sessionViewModel.disableBars()
    }

    LaunchedEffect(uploadResult) {
        if (uploadResult is MiraiLinkResult.Success && userId != null) {
            viewModel.clearResult()
            sessionViewModel.refreshHasProfilePicture(userId)
            onProfileUploaded()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logomirailink),
            contentDescription = "Profile picture placeholder",
            modifier = Modifier
                .size(128.dp)
                .clip(
                    CircleShape
                )
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Seleccionar imagen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uploadResult is MiraiLinkResult.Error) {
            Text("Error al subir. Inténtalo de nuevo.", color = MaterialTheme.colorScheme.error)
        }
    }

    BackHandler(enabled = true) { Log.i("OnBack", "Clicked back on Profile Picture Upload Screen") }
}