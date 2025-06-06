package com.feryaeljustice.mirailink.ui.screens.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.feryaeljustice.mirailink.data.util.createImageUri
import com.feryaeljustice.mirailink.ui.components.UserCard
import com.feryaeljustice.mirailink.ui.screens.profile.ProfileViewModel.ProfileUiState
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileIntent
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel, sessionViewModel: GlobalSessionViewModel) {
    val state by viewModel.state.collectAsState()
    val editState by viewModel.editState.collectAsState()
    var isInEditMode by rememberSaveable { mutableStateOf(false) }

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

    // Permiso de cámara
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                val uri = createImageUri(context)
                tempCameraUri = uri
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Se necesita permiso de cámara", Toast.LENGTH_SHORT).show()
            }
        }

    LaunchedEffect(Unit) {
        sessionViewModel.showBars()
        sessionViewModel.enableBars()
        sessionViewModel.showTopBarSettingsIcon()
    }

    LaunchedEffect(isInEditMode) {
        if (!isInEditMode) {
            viewModel.cleanupTempPhotos()
        }
    }

    PullToRefreshBox(isRefreshing = state is ProfileUiState.Loading, onRefresh = {
        viewModel.getCurrentUser()
    }, modifier = Modifier.fillMaxSize()) {
        when (state) {
            is ProfileUiState.Success -> {
                (state as ProfileUiState.Success).user?.let { user ->
                    Box(modifier = Modifier.padding(16.dp)) {
                        UserCard(
                            user = user,
                            isPreviewMode = true,
                            isEditMode = isInEditMode,
                            editUiState = editState,
                            onEdit = { isEdit -> isInEditMode = isEdit },
                            onValueChange = { field, value ->
                                Log.d("ProfileScreen", "onValueChange: $field $value")
                                viewModel.onIntent(
                                    EditProfileIntent.UpdateTextField(
                                        field, value
                                    )
                                )
                            },
                            onTagSelected = { field, value ->
                                Log.d(
                                    "ProfileScreen",
                                    "onTagSelected: $field, value "
                                )
                                viewModel.onIntent(
                                    EditProfileIntent.UpdateTags(
                                        field, value
                                    )
                                )
                            },
                            onPhotoReorder = { oldPosition, newPosition ->
                                Log.d(
                                    "ProfileScreen",
                                    "onPhotoReorder: $oldPosition $newPosition"
                                )
                                viewModel.onIntent(
                                    EditProfileIntent.ReorderPhoto(
                                        oldPosition,
                                        newPosition
                                    )
                                )
                            },
                            onPhotoSlotClick = { position ->
                                // position is the index of the photo slot
                                Log.d(
                                    "ProfileScreen",
                                    "onPhotoSlotClick: $position"
                                )
                                viewModel.onIntent(EditProfileIntent.OpenPhotoActionDialog(position))
                            },
                            modifier = Modifier
                                .padding(2.dp)
                        )

                        // 1. Dialogo: Actualizar o Borrar
                        if (editState.showActionDialog && editState.selectedSlotForDialog != null) {
                            AlertDialog(
                                onDismissRequest = { viewModel.onIntent(EditProfileIntent.ClosePhotoDialogs) },
                                title = { Text("¿Qué quieres hacer?") },
                                text = { Text("Selecciona una acción para esta foto") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        viewModel.onIntent(EditProfileIntent.ShowPhotoSourceDialog)
                                    }) {
                                        Text("Actualizar")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = {
                                        editState.selectedSlotForDialog?.let {
                                            viewModel.onIntent(
                                                EditProfileIntent.RemovePhoto(it)
                                            )
                                            viewModel.onIntent(EditProfileIntent.ClosePhotoDialogs)
                                        }
                                    }) {
                                        Text("Eliminar")
                                    }
                                }
                            )
                        }

                        // 2. Dialogo: Galería o Cámara
                        if (editState.showPhotoSourceDialog && editState.selectedSlotForDialog != null) {
                            AlertDialog(
                                onDismissRequest = { viewModel.onIntent(EditProfileIntent.ClosePhotoDialogs) },
                                title = { Text("Seleccionar fuente") },
                                text = { Text("¿Desde dónde quieres añadir la foto?") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        // Aquí lanzas launcher de galería
                                        Log.d("ProfileScreen", "Elegido: Galería")
                                        galleryLauncher.launch("image/*")
                                    }) {
                                        Text("Galería")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = {
                                        // Aquí lanzas launcher de cámara
                                        Log.d("ProfileScreen", "Elegido: Cámara")
                                        if (ContextCompat.checkSelfPermission(
                                                context,
                                                Manifest.permission.CAMERA
                                            ) == PackageManager.PERMISSION_GRANTED
                                        ) {
                                            val uri = createImageUri(context)
                                            tempCameraUri = uri
                                            cameraLauncher.launch(uri)
                                        } else {
                                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                            viewModel.onIntent(EditProfileIntent.ClosePhotoDialogs)
                                        }
                                    }) {
                                        Text("Cámara")
                                    }
                                }
                            )
                        }
                    }
                }
            }

            is ProfileUiState.Error -> {
                val error = state as ProfileUiState.Error
                Text(
                    text = error.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            ProfileUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            ProfileUiState.Idle -> Unit
        }
    }
}