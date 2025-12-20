package com.feryaeljustice.mirailink.ui.screens.photo

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import org.koin.compose.viewmodel.koinViewModel

@Suppress("EffectKeys", "ParamsComparedByRef", "ktlint:standard:function-naming")
@Composable
fun ProfilePictureScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    onProfileUpload: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfilePictureViewModel = koinViewModel(),
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val currentOnProfileUpload by rememberUpdatedState(onProfileUpload)

    val userId by miraiLinkSession.currentUserId.collectAsStateWithLifecycle()
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { viewModel.uploadImage(it) }
        }
    val uploadResult by viewModel.uploadResult.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        miraiLinkSession.showBars()
        miraiLinkSession.disableBars()
    }

    LaunchedEffect(uploadResult) {
        if (uploadResult is MiraiLinkResult.Success && !userId.isNullOrBlank()) {
            viewModel.clearResult()
            miraiLinkSession.refreshHasProfilePicture(userId!!)
            currentOnProfileUpload()
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(32.dp)
                .then(
                    if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                        Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                    } else {
                        Modifier
                    },
                ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier =
                Modifier
                    .size(128.dp)
                    .clip(
                        CircleShape,
                    ).background(Color.Gray),
            painter = painterResource(id = R.drawable.logomirailink),
            contentDescription = stringResource(R.string.content_description_profile_picture_screen_profilepicplaceholder),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            MiraiLinkText(
                text = stringResource(R.string.profile_picture_screen_select_img),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        MiraiLinkText(
            text = stringResource(R.string.logout),
            modifier =
                Modifier.clickable(role = Role.Button, onClick = {
                    miraiLinkSession.clearSession()
                }),
            fontStyle = MaterialTheme.typography.labelMedium.fontStyle,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uploadResult is MiraiLinkResult.Error) {
            MiraiLinkText(
                text = stringResource(R.string.profile_picture_screen_upload_error),
                color = MaterialTheme.colorScheme.error,
            )
        }
    }

    BackHandler(enabled = true) { Log.i("OnBack", "Clicked back on Profile Picture Upload Screen") }
}
