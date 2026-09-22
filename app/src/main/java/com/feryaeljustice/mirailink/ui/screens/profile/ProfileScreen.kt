package com.feryaeljustice.mirailink.ui.screens.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import com.feryaeljustice.mirailink.domain.enums.TextFieldType
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorSnackbar
import com.feryaeljustice.mirailink.ui.components.user.UserCard
import com.feryaeljustice.mirailink.ui.screens.profile.ProfileViewModel.ProfileUiState
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileIntent
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiEvent
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import com.feryaeljustice.mirailink.ui.utils.toast.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.viewmodel.koinViewModel

// NO SE PUEDE porque las previews no tienen las librerias de android, van en jvm, hay que mockear: Define un Módulo de Koin para Previews
@Suppress("ktlint:standard:no-consecutive-comments","ktlint:standard:function-naming", "ParamsComparedByRef", "EffectKeys")
/*
@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    val context = LocalContext.current

    KoinApplication(application = {
        androidContext(context)
        modules(MiraiLinkApp.coreModules + MiraiLinkApp.infrastructureModules + MiraiLinkApp.securityModules)
    }) {
        val miraiLinkSession: GlobalMiraiLinkSession = koinInject()
        ProfileScreen(
            miraiLinkSession = miraiLinkSession,
            modifier = Modifier,
        )
    }
}
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val state by viewModel.state.collectAsStateWithLifecycle()
    val editState by viewModel.editState.collectAsStateWithLifecycle()
    val isDemoMode by miraiLinkSession.isDemoMode.collectAsStateWithLifecycle()
    val currentUserId by miraiLinkSession.currentUserId.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

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
    var pendingCameraSlot by rememberSaveable { mutableStateOf<Int?>(null) }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                tempCameraUri?.let {
                    val index = pendingCameraSlot ?: return@rememberLauncherForActivityResult
                    viewModel.onIntent(EditProfileIntent.UpdatePhoto(index, it))
                }
            }
            tempCameraUri = null
            pendingCameraSlot = null
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
                pendingCameraSlot = null
                showToast(
                    context,
                    needsCameraPermissionText,
                    Toast.LENGTH_SHORT,
                )
            }
        }

    var locationPermissionRequestVersion by rememberSaveable { mutableIntStateOf(0) }
    val locationPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            ) {
                locationPermissionRequestVersion++
            }
        }

    val profileSavedSuccessfullyText =
        stringResource(R.string.profile_screen_profile_saved_correctly)
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
            }
        }
    }

    LaunchedEffect(editState.isEditing, locationPermissionRequestVersion) {
        if (!editState.isEditing) {
            viewModel.cleanupTempPhotos()
        } else if (
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                    ?: return@LaunchedEffect
            val providers = buildList {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) add(LocationManager.GPS_PROVIDER)
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) add(LocationManager.NETWORK_PROVIDER)
            }
            providers
                .mapNotNull(locationManager::getLastKnownLocation)
                .maxByOrNull(Location::getTime)
                ?.let { location -> residenceAddress(context, location)?.let(viewModel::updateResidence) }

            providers.forEach { provider ->
                runCatching {
                    locationManager.getCurrentLocation(
                        provider,
                        null,
                        ContextCompat.getMainExecutor(context),
                    ) { location ->
                        location ?: return@getCurrentLocation
                        coroutineScope.launch {
                            residenceAddress(context, location)?.let { address ->
                                if (viewModel.editState.value.isEditing) viewModel.updateResidence(address)
                            }
                        }
                    }
                }
            }
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
        }
    }

    LaunchedEffect(isDemoMode, currentUserId) {
        viewModel.getCurrentUser()
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
        AnimatedContent(
            targetState = state,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                (fadeIn() + scaleIn(initialScale = 0.92f))
                    .togetherWith(fadeOut() + scaleOut(targetScale = 0.92f))
            },
            label = "ProfileStateTransition",
        ) { currentState ->
            when (currentState) {
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
                                onResidenceCoordinatesSelected = { latitude, longitude ->
                                    viewModel.onIntent(EditProfileIntent.UpdateResidenceCoordinates(latitude, longitude))
                                },
                                onResidencePlaceSelected = { field, place ->
                                    viewModel.onIntent(
                                        EditProfileIntent.SelectResidencePlace(
                                            field = field,
                                            id = place.id,
                                            name = place.name,
                                            latitude = place.latitude,
                                            longitude = place.longitude,
                                        ),
                                    )
                                },
                                onResidenceTextChanged = { field, value ->
                                    viewModel.onIntent(EditProfileIntent.EditResidenceText(field, value))
                                },
                                onResidenceFieldCleared = { field ->
                                    viewModel.onIntent(EditProfileIntent.ClearResidenceField(field))
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

                            // 2. Dialogo: Galeria o Camara
                            if (editState.showPhotoSourceDialog && editState.selectedSlotForDialog != null) {
                                AlertDialog(
                                    onDismissRequest = { viewModel.onIntent(EditProfileIntent.ClosePhotoDialogs) },
                                    title = { MiraiLinkText(text = stringResource(R.string.profile_screen_dialog_media_origin_title)) },
                                    text = { MiraiLinkText(text = stringResource(R.string.profile_screen_dialog_media_origin_text)) },
                                    confirmButton = {
                                        MiraiLinkTextButton(
                                            onClick = {
                                                // Aqui lanzas launcher de galeria
                                                Log.d("ProfileScreen", "Chosen: Gallery")
                                                galleryLauncher.launch("image/*")
                                            },
                                            text = stringResource(R.string.gallery),
                                        )
                                    },
                                    dismissButton = {
                                        MiraiLinkTextButton(
                                            onClick = {
                                                // Aqui lanzas launcher de camara
                                                Log.d("ProfileScreen", "Chosen: Camera")
                                                pendingCameraSlot = editState.selectedSlotForDialog
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
                    MiraiLinkErrorContent(
                        error = currentState.error,
                        onAction = viewModel::performErrorAction,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                ProfileUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                ProfileUiState.Idle -> {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }

        // Keep save errors above the editable card so the retry action remains visible.
        editState.error?.let { error ->
            MiraiLinkErrorSnackbar(
                error = error,
                onAction = viewModel::performErrorAction,
            )
        }
    }
}

private suspend fun residenceAddress(context: Context, location: Location) =
    if (!Geocoder.isPresent()) {
        null
    } else {
        withContext(Dispatchers.IO) {
            runCatching {
                @Suppress("DEPRECATION")
                Geocoder(context).getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()
            }.getOrNull()
        }
    }

private fun ProfileViewModel.updateResidence(address: android.location.Address) {
    address.countryCode?.let { code ->
        val countryName = java.util.Locale.Builder().setRegion(code).build().getDisplayCountry(java.util.Locale.getDefault())
        onIntent(EditProfileIntent.UpdateTextField(TextFieldType.RESIDENCE_COUNTRY, countryName))
    }
    address.adminArea?.let { region ->
        onIntent(EditProfileIntent.UpdateTextField(TextFieldType.RESIDENCE_REGION, region))
    }
    address.locality?.let { city ->
        onIntent(EditProfileIntent.UpdateTextField(TextFieldType.RESIDENCE_CITY, city))
    }
    onIntent(EditProfileIntent.UpdateResidenceCoordinates(address.latitude, address.longitude))
}
