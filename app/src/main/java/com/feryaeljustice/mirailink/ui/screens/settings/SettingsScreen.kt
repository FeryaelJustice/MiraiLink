package com.feryaeljustice.mirailink.ui.screens.settings

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.BuildConfig
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.constants.deepLinkPrivacyPolicyUrl
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorPutCodeOrRecoveryCDialog
import com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorSetupDialog
import com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorSetupCompletedDialog
import com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorStatusDialog
import com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure.ConfigureTwoFactorViewModel
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import org.koin.compose.viewmodel.koinViewModel

@Suppress("ktlint:standard:function-naming", "ParamsComparedByRef", "EffectKeys")
@Composable
fun SettingsScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    goToFeedbackScreen: () -> Unit,
    goToFaqScreen: () -> Unit,
    showToast: (String, Int) -> Unit,
    copyToClipBoard: (String) -> Unit,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    twoFactorViewModel: ConfigureTwoFactorViewModel = koinViewModel(),
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val actualGoToFeedbackScreen by rememberUpdatedState(goToFeedbackScreen)
    val actualGoToFaqScreen by rememberUpdatedState(goToFaqScreen)

    val draftRadius by viewModel.draftRadiusKm.collectAsStateWithLifecycle()
    val draftScope by viewModel.draftScope.collectAsStateWithLifecycle()
    val draftTargetCountry by viewModel.draftTargetCountry.collectAsStateWithLifecycle()
    val draftMatchLiveLocation by viewModel.draftMatchLiveLocation.collectAsStateWithLifecycle()
    val hasUnsavedChanges by viewModel.hasUnsavedChanges.collectAsStateWithLifecycle()
    val isSavingPreferences by viewModel.isSavingPreferences.collectAsStateWithLifecycle()
    val userLat by viewModel.userLatitude.collectAsStateWithLifecycle()
    val userLon by viewModel.userLongitude.collectAsStateWithLifecycle()

    val uriHandler = LocalUriHandler.current

    var showDeleteDialog by remember { mutableStateOf(false) }
    val error by viewModel.error.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }
    val userId by miraiLinkSession.currentUserId.collectAsStateWithLifecycle()
    val isTwoFactorEnabled by twoFactorViewModel.isTwoFactorEnabled.collectAsStateWithLifecycle()
    val showTwoFactorStatusDialog by twoFactorViewModel.showStatusDialog.collectAsStateWithLifecycle()
    val showTwoFactorSetupDialog by twoFactorViewModel.showSetupDialog.collectAsStateWithLifecycle()
    val showTwoFactorSetupCompletedDialog by twoFactorViewModel.showSetupCompletedDialog.collectAsStateWithLifecycle()
    val showTwoFactorDisableDialog by twoFactorViewModel.showDisableTwoFactorDialog.collectAsStateWithLifecycle()
    val twoFactorDisableLoading by twoFactorViewModel.isDisable2FALoading.collectAsStateWithLifecycle()
    val twoFactorSetupLoading by twoFactorViewModel.isConfigure2FALoading.collectAsStateWithLifecycle()
    val twoFactorStartingSetup by twoFactorViewModel.isStartingSetup.collectAsStateWithLifecycle()
    val twoFactorOtpUrl by twoFactorViewModel.otpUrl.collectAsStateWithLifecycle()
    val twoFactorBase32 by twoFactorViewModel.base32.collectAsStateWithLifecycle()
    val twoFactorRecoveryCodes by twoFactorViewModel.recoveryCodes.collectAsStateWithLifecycle()
    val twoFactorSetupCode by twoFactorViewModel.verify2FACode.collectAsStateWithLifecycle()
    val twoFactorDisableCode by twoFactorViewModel.disable2FACode.collectAsStateWithLifecycle()
    val twoFactorError by twoFactorViewModel.errorString.collectAsStateWithLifecycle()

    if (showTwoFactorStatusDialog) {
        TwoFactorStatusDialog(
            enabled = isTwoFactorEnabled,
            onDismiss = twoFactorViewModel::dismissStatusDialog,
            onEnable = twoFactorViewModel::launchActivationFromStatus,
            onDisable = {
                twoFactorViewModel.dismissStatusDialog()
                twoFactorViewModel.launchDisableTwoFactorDialog()
            },
        )
    }

    if (showTwoFactorSetupDialog) {
        TwoFactorSetupDialog(
            otpUrl = twoFactorOtpUrl,
            base32 = twoFactorBase32,
            recoveryCodes = twoFactorRecoveryCodes,
            code = twoFactorSetupCode,
            isLoading = twoFactorSetupLoading || twoFactorStartingSetup,
            onCodeChange = twoFactorViewModel::onSetupTwoFactorCodeChanged,
            onDismiss = twoFactorViewModel::dismissSetupTwoFactorDialog,
            onConfirm = { twoFactorViewModel.confirmSetupTwoFactor(userId) },
        )
    }

    if (showTwoFactorSetupCompletedDialog) {
        TwoFactorSetupCompletedDialog(
            onDismiss = twoFactorViewModel::dismissSetupCompletedDialog,
        )
    }

    if (showTwoFactorDisableDialog) {
        TwoFactorPutCodeOrRecoveryCDialog(
            code = twoFactorDisableCode,
            isLoading = twoFactorDisableLoading,
            onCodeChange = twoFactorViewModel::onDisableTwoFactorCodeChanged,
            onDismiss = twoFactorViewModel::dismissDisableTwoFactorDialog,
            onConfirm = { twoFactorViewModel.confirmDisableTwoFactor(userId) },
        )
    }

    twoFactorError?.let { errorMessage ->
        MiraiLinkErrorContent(
            error = errorMessage,
            onAction = twoFactorViewModel::performErrorAction,
        )
    }

    LaunchedEffect(Unit) {
        miraiLinkSession.showBars()
        miraiLinkSession.enableBars()
        miraiLinkSession.hideTopBarSettingsIcon()
    }

    LaunchedEffect(Unit) {
        viewModel.logoutSuccess.collect { success ->
            if (success) miraiLinkSession.clearSession()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.deleteSuccess.collect { success ->
            if (success) miraiLinkSession.clearSession()
        }
    }

    val delAccountDoneText = stringResource(R.string.delete_account_done)
    AnimatedVisibility(showDeleteDialog) {
        MiraiLinkDialog(
            title = stringResource(R.string.delete_account_confirm_title),
            message = stringResource(R.string.delete_account_confirm_text),
            onDismiss = { showDeleteDialog = false },
            onAccept = {
                showDeleteDialog = false
                viewModel.deleteAccount {
                    showToast(
                        delAccountDoneText,
                        Toast.LENGTH_SHORT,
                    )
                }
            },
            onCancel = { showDeleteDialog = false },
            acceptText = stringResource(R.string.accept),
            cancelText = stringResource(R.string.cancel),
            containerColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface,
            buttonTextColor = MaterialTheme.colorScheme.onPrimary,
        )
    }

    val logoutDoneText = stringResource(R.string.logout_done)
    AnimatedVisibility(showLogoutDialog) {
        MiraiLinkDialog(
            title = stringResource(R.string.logout_account_confirm_title),
            onDismiss = { showLogoutDialog = false },
            onAccept = {
                showLogoutDialog = false
                viewModel.logout(onFinish = {
                    showToast(logoutDoneText, Toast.LENGTH_SHORT)
                })
            },
            onCancel = { showLogoutDialog = false },
            acceptText = stringResource(R.string.accept),
            cancelText = stringResource(R.string.cancel),
            containerColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface,
            buttonTextColor = MaterialTheme.colorScheme.onPrimary,
        )
    }

    val isDemoMode by miraiLinkSession.isDemoMode.collectAsStateWithLifecycle()
    val resetDemoDoneText = stringResource(R.string.demo_mode_reset_data_done)

    val scrollState = rememberScrollState()
    var isMapVisible by remember { mutableStateOf(false) }

    // El mapa se hace visible en cuanto el usuario empieza a hacer scroll
    LaunchedEffect(scrollState.isScrollInProgress) {
        if (scrollState.isScrollInProgress) {
            isMapVisible = true
        }
    }

    // O en cuanto cambie algún parámetro del borrador
    LaunchedEffect(draftRadius, draftScope, draftTargetCountry, draftMatchLiveLocation) {
        if (hasUnsavedChanges) {
            isMapVisible = true
        }
    }

    // Permisos de Ubicación (Precisa y Aproximada) con compatibilidad hacia atrás
    val context = androidx.compose.ui.platform.LocalContext.current
    var showLocationRationaleDialog by remember { mutableStateOf(false) }

    val locationPermissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val fineGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineGranted || coarseGranted) {
            // Ubicación otorgada (precisa o aproximada)
            try {
                val locationManager =
                    context.getSystemService(android.content.Context.LOCATION_SERVICE) as? android.location.LocationManager
                if (locationManager != null) {
                    val isGpsEnabled =
                        locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
                    val isNetworkEnabled =
                        locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)

                    var lastKnown: android.location.Location? = null
                    if (fineGranted && isGpsEnabled) {
                        lastKnown =
                            locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER)
                    }
                    if (lastKnown == null && (fineGranted || coarseGranted) && isNetworkEnabled) {
                        lastKnown =
                            locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER)
                    }

                    lastKnown?.let { loc ->
                        viewModel.updateUserCoordinates(loc.latitude, loc.longitude)
                    }
                }
            } catch (_: SecurityException) {
                // Ignore security exception
            }
        }
        showLocationRationaleDialog = false
    }

    val requestLocationPermissions = {
        val hasFine = androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        val hasCoarse = androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) {
            val activity = context as? android.app.Activity
            val shouldShowFine = activity?.let {
                androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                )
            } ?: false

            val shouldShowCoarse = activity?.let {
                androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            } ?: false

            if (shouldShowFine || shouldShowCoarse) {
                showLocationRationaleDialog = true
            } else {
                locationPermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    ),
                )
            }
        }
    }

    AnimatedVisibility(showLocationRationaleDialog) {
        MiraiLinkDialog(
            title = stringResource(R.string.location_permission_rationale_title),
            message = stringResource(R.string.location_permission_rationale_desc),
            onDismiss = { showLocationRationaleDialog = false },
            onAccept = {
                showLocationRationaleDialog = false
                locationPermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    ),
                )
            },
            onCancel = { showLocationRationaleDialog = false },
            acceptText = stringResource(R.string.accept),
            cancelText = stringResource(R.string.cancel),
        )
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .then(
                    if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                        Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                    } else {
                        Modifier
                    },
                )
                .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MiraiLinkIconButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = onBackClick,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                MiraiLinkText(
                    text = stringResource(R.string.settings_screen_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                )
                MiraiLinkText(
                    text = stringResource(R.string.settings_screen_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(24.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        error?.let { currentError ->
            MiraiLinkErrorContent(
                error = currentError,
                onAction = viewModel::performErrorAction,
            )
        }
        // Logo de MiraiLink redimensionado a tamaño más compacto y elegante
        Image(
            painter = painterResource(id = R.drawable.logomirailink),
            contentDescription = stringResource(R.string.content_description_settings_screen_img_logo),
            modifier =
                Modifier
                    .size(110.dp)
                    .padding(4.dp),
        )
        val searchSavedText = stringResource(R.string.search_settings_saved_success)

        SettingsSectionTitle(stringResource(R.string.settings_section_experience))
        // Seccion de Preferencias de Busqueda (Minimapa condicional, Radio, Pais, Viajeros)
        com.feryaeljustice.mirailink.ui.screens.settings.components.SearchSettingsSection(
            radiusKm = draftRadius,
            onRadiusChange = { radius ->
                isMapVisible = true
                viewModel.updateDraftRadius(radius)
            },
            scope = draftScope,
            onScopeChange = { scope ->
                isMapVisible = true
                viewModel.updateDraftScope(scope)
            },
            targetCountry = draftTargetCountry,
            onTargetCountryChange = { country ->
                isMapVisible = true
                viewModel.updateDraftTargetCountry(country)
            },
            matchLiveLocation = draftMatchLiveLocation,
            onMatchLiveLocationChange = { enabled ->
                isMapVisible = true
                viewModel.updateDraftMatchLiveLocation(enabled)
            },
            hasUnsavedChanges = hasUnsavedChanges,
            isSaving = isSavingPreferences,
            onSaveClick = {
                viewModel.saveSearchPreferences {
                    isMapVisible = false // Al guardar ajustes se oculta de nuevo el mapa
                    showToast(searchSavedText, Toast.LENGTH_SHORT)
                }
            },
            latitude = userLat,
            longitude = userLon,
            isMapVisible = isMapVisible,
            onRequestLocationPermission = requestLocationPermissions,
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsActionCard(
            icon = Icons.Default.Info,
            title = stringResource(R.string.faq_title),
            subtitle = stringResource(R.string.settings_faq_subtitle),
            onClick = { actualGoToFaqScreen() },
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isDemoMode) {
            SettingsActionCard(
                icon = Icons.Default.Refresh,
                title = stringResource(R.string.demo_mode_reset_data),
                subtitle = stringResource(R.string.settings_reset_demo_subtitle),
                onClick = {
                    miraiLinkSession.resetDemoData {
                        showToast(resetDemoDoneText, Toast.LENGTH_SHORT)
                    }
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsActionCard(
                icon = Icons.Default.ExitToApp,
                title = stringResource(R.string.demo_mode_exit),
                subtitle = stringResource(R.string.settings_exit_demo_subtitle),
                onClick = miraiLinkSession::clearSession,
            )
        } else {
            SettingsSectionTitle(stringResource(R.string.settings_section_account))
            SettingsActionCard(
                icon = Icons.Default.Favorite,
                title = stringResource(R.string.settings_screen_txt_give_feedback),
                subtitle = stringResource(R.string.settings_feedback_subtitle),
                onClick = { actualGoToFeedbackScreen() },
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsActionCard(
                icon = Icons.Default.Lock,
                title = stringResource(R.string.configure_two_factor),
                subtitle = stringResource(R.string.settings_two_factor_subtitle),
                onClick = { userId?.let(twoFactorViewModel::onlyCheckTwoFacStatusWithIO) },
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsActionCard(
                icon = Icons.Default.ExitToApp,
                title = stringResource(R.string.logout),
                subtitle = stringResource(R.string.settings_logout_subtitle),
                onClick = { showLogoutDialog = true },
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsActionCard(
                icon = Icons.Default.Delete,
                title = stringResource(R.string.delete_account),
                subtitle = stringResource(R.string.settings_delete_account_subtitle),
                onClick = { showDeleteDialog = true },
                destructive = true,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        SettingsSectionTitle(stringResource(R.string.settings_section_about))
        SettingsActionCard(
            icon = Icons.Default.Lock,
            title = stringResource(R.string.privacy_policy),
            subtitle = stringResource(R.string.settings_privacy_subtitle),
            onLongPress = { copyToClipBoard(deepLinkPrivacyPolicyUrl) },
            onClick = { uriHandler.openUri(deepLinkPrivacyPolicyUrl) },
        )

        Row(
            modifier =
                Modifier
                    .padding(top = 12.dp, bottom = 24.dp)
                    .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MiraiLinkText(
                text = stringResource(R.string.version_app, BuildConfig.VERSION_NAME),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    MiraiLinkText(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 8.dp, start = 20.dp),
    )
}

@Composable
private fun SettingsActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    onLongPress: (() -> Unit)? = null,
    destructive: Boolean = false,
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = { onLongPress?.invoke() })
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (destructive) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainerLow
            },
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = CircleShape,
                color = if (destructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondaryContainer,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (destructive) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(22.dp),
                )
            }
            Spacer(Modifier.size(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                MiraiLinkText(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                    color = if (destructive) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface,
                )
                MiraiLinkText(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (destructive) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
