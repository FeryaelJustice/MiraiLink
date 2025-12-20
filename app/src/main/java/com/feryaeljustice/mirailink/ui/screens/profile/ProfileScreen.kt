package com.feryaeljustice.mirailink.ui.screens.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.data.util.createImageUri
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton
import com.feryaeljustice.mirailink.ui.components.user.UserCard
import com.feryaeljustice.mirailink.ui.screens.profile.ProfileViewModel.ProfileUiState
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileIntent
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiEvent
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import com.feryaeljustice.mirailink.ui.utils.toast.showToast
import org.koin.compose.viewmodel.koinViewModel

@Suppress("ktlint:standard:function-naming", "ParamsComparedByRef", "EffectKeys")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val state by viewModel.state.collectAsStateWithLifecycle()
    val editState by viewModel.editState.collectAsStateWithLifecycle()

    // Galería
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val index =
                    editState.selectedSlotForDialog ?: return@rememberLauncherForActivityResult
                viewModel.onIntent(EditProfileIntent.UpdatePhoto(index, it))
                viewModel.onIntent(EditProfileIntent.ClosePhotoDialogs)
            }
        }

    // Cámara (usando URI temporal con FileProvider)
    val context = LocalContext.current
    var tempCameraUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                tempCameraUri?.let {
                    val index =
                        editState.selectedSlotForDialog ?: return@rememberLauncherForActivityResult
                    viewModel.onIntent(EditProfileIntent.UpdatePhoto(index, it))
                    viewModel.onIntent(EditProfileIntent.ClosePhotoDialogs)
                }
            }
        }

    val needsCameraPermissionText = stringResource(R.string.need_camera_permission)
    // Permiso de cámara
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                val uri = createImageUri(context)
                tempCameraUri = uri
                cameraLauncher.launch(uri)
            } else {
                showToast(
                    context,
                    needsCameraPermissionText,
                    Toast.LENGTH_SHORT,
                )
            }
        }

    val profileSavedSuccessfullyText = stringResource(R.string.profile_screen_profile_saved_correctly)
    LaunchedEffect(Unit) {
        miraiLinkSession.showBars()
        miraiLinkSession.enableBars()
        miraiLinkSession.showTopBarSettingsIcon()

        viewModel.editProfUiEvent.collect { event ->
            when (event) {
                EditProfileUiEvent.ProfileSavedSuccessfully -> {
                    showToast(
                        context,
                        profileSavedSuccessfullyText,
                        Toast.LENGTH_SHORT,
                    )
                }

                is EditProfileUiEvent.ShowError -> {
                    showToast(context, event.message, Toast.LENGTH_LONG)
                }
            }
        }
    }

    LaunchedEffect(editState.isEditing) {
        if (!editState.isEditing) {
            viewModel.cleanupTempPhotos()
        }
    }

    PullToRefreshBox(
        isRefreshing = state is ProfileUiState.Loading,
        onRefresh = {
            viewModel.getCurrentUser()
        },
        modifier =
            modifier
                .fillMaxSize()
                .then(
                    if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                        Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                    } else {
                        Modifier
                    },
                ),
    ) {
        when (val currentState = state) {
            is ProfileUiState.Success -> {
                currentState.user?.let { user ->
                    Box(modifier = Modifier.padding(16.dp)) {
                        UserCard(
                            modifier =
                                Modifier
                                    .padding(2.dp),
                            user = user,
                            isPreviewMode = true,
                            editUiState = editState,
                            onEdit = { isEdit ->
                                viewModel.setIsInEditMode(isEdit)

                                // Initialize edit state if going to edit user
                                if (isEdit) {
                                    (state as? ProfileUiState.Success)?.user?.let { stateUser ->
                                        viewModel.onIntent(EditProfileIntent.Initialize(stateUser))
                                    }
                                }
                            },
                            onSave = {
                                viewModel.onIntent(EditProfileIntent.Save)
                            },
                            onValueChange = { field, value ->
                                Log.d("ProfileScreen", "onValueChange: $field $value")
                                viewModel.onIntent(
                                    EditProfileIntent.UpdateTextField(
                                        field,
                                        value,
                                    ),
                                )
                            },
                            onTagSelect = { field, value ->
                                Log.d(
                                    "ProfileScreen",
                                    "onTagSelect: $field, value ",
                                )
                                viewModel.onIntent(
                                    EditProfileIntent.UpdateTags(
                                        field,
                                        value,
                                    ),
                                )
                            },
                            onPhotoReorder = { oldPosition, newPosition ->
                                Log.d(
                                    "ProfileScreen",
                                    "onPhotoReorder: $oldPosition $newPosition",
                                )
                                viewModel.onIntent(
                                    EditProfileIntent.ReorderPhoto(
                                        oldPosition,
                                        newPosition,
                                    ),
                                )
                            },
                            onPhotoSlotClick = { position ->
                                // position is the index of the photo slot
                                Log.d(
                                    "ProfileScreen",
                                    "onPhotoSlotClick: $position",
                                )
                                viewModel.onIntent(EditProfileIntent.OpenPhotoActionDialog(position))
                            },
                        )

                        // 1. Dialogo: Actualizar o Borrar
                        if (editState.showActionDialog && editState.selectedSlotForDialog != null) {
                            AlertDialog(
                                onDismissRequest = { viewModel.onIntent(EditProfileIntent.ClosePhotoDialogs) },
                                title = { MiraiLinkText(text = stringResource(R.string.profile_screen_dialog_action_title)) },
                                text = { MiraiLinkText(text = stringResource(R.string.profile_screen_dialog_action_text)) },
                                confirmButton = {
                                    MiraiLinkTextButton(
                                        onClick = {
                                            viewModel.onIntent(EditProfileIntent.ShowPhotoSourceDialog)
                                        },
                                        text = stringResource(R.string.update),
                                    )
                                },
                                dismissButton = {
                                    MiraiLinkTextButton(
                                        onClick = {
                                            editState.selectedSlotForDialog?.let {
                                                viewModel.onIntent(
                                                    EditProfileIntent.RemovePhoto(it),
                                                )
                                                viewModel.onIntent(EditProfileIntent.ClosePhotoDialogs)
                                            }
                                        },
                                        text = stringResource(R.string.delete),
                                    )
                                },
                            )
                        }

                        // 2. Dialogo: Galería o Cámara
                        if (editState.showPhotoSourceDialog && editState.selectedSlotForDialog != null) {
                            AlertDialog(
                                onDismissRequest = { viewModel.onIntent(EditProfileIntent.ClosePhotoDialogs) },
                                title = { MiraiLinkText(text = stringResource(R.string.profile_screen_dialog_media_origin_title)) },
                                text = { MiraiLinkText(text = stringResource(R.string.profile_screen_dialog_media_origin_text)) },
                                confirmButton = {
                                    MiraiLinkTextButton(
                                        onClick = {
                                            // Aquí lanzas launcher de galería
                                            Log.d("ProfileScreen", "Chosen: Gallery")
                                            galleryLauncher.launch("image/*")
                                        },
                                        text = stringResource(R.string.gallery),
                                    )
                                },
                                dismissButton = {
                                    MiraiLinkTextButton(
                                        onClick = {
                                            // Aquí lanzas launcher de cámara
                                            Log.d("ProfileScreen", "Chosen: Camera")
                                            if (ContextCompat.checkSelfPermission(
                                                    context,
                                                    Manifest.permission.CAMERA,
                                                ) == PackageManager.PERMISSION_GRANTED
                                            ) {
                                                val uri = createImageUri(context)
                                                tempCameraUri = uri
                                                cameraLauncher.launch(uri)
                                            } else {
                                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                                viewModel.onIntent(EditProfileIntent.ClosePhotoDialogs)
                                            }
                                        },
                                        text = stringResource(R.string.camera),
                                    )
                                },
                            )
                        }
                    }
                }
            }

            is ProfileUiState.Error -> {
                MiraiLinkText(
                    text = currentState.message,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                )
            }

            ProfileUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            ProfileUiState.Idle -> {}
        }
    }
}
