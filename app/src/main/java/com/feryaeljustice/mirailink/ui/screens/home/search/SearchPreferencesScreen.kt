package com.feryaeljustice.mirailink.ui.screens.home.search

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.settings.SearchScope
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import com.feryaeljustice.mirailink.ui.screens.settings.components.SearchSettingsSection
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchPreferencesScreen(
    onBackClick: () -> Unit,
    showToast: (String, Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchPreferencesViewModel = koinViewModel(),
) {
    val radiusKm by viewModel.draftRadiusKm.collectAsStateWithLifecycle()
    val scope by viewModel.draftScope.collectAsStateWithLifecycle()
    val targetCountry by viewModel.draftTargetCountry.collectAsStateWithLifecycle()
    val matchLiveLocation by viewModel.draftMatchLiveLocation.collectAsStateWithLifecycle()
    val hasUnsavedChanges by viewModel.hasUnsavedChanges.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSavingPreferences.collectAsStateWithLifecycle()
    val latitude by viewModel.userLatitude.collectAsStateWithLifecycle()
    val longitude by viewModel.userLongitude.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var isMapVisible by remember { mutableStateOf(false) }
    var isRefreshingLocation by remember { mutableStateOf(false) }
    var showLocationRationale by remember { mutableStateOf(false) }

    fun readCurrentLocation(onFinished: () -> Unit = {}) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return onFinished()
        val fineGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!fineGranted && !coarseGranted) return onFinished()
        val providers = buildList {
            if (fineGranted && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) add(LocationManager.GPS_PROVIDER)
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) add(LocationManager.NETWORK_PROVIDER)
        }.distinct()
        providers.mapNotNull(locationManager::getLastKnownLocation).maxByOrNull { it.time }?.let {
            viewModel.updateUserCoordinates(it.latitude, it.longitude)
        }
        if (providers.isEmpty()) return onFinished()
        try {
            providers.forEach { provider ->
                locationManager.getCurrentLocation(provider, null, ContextCompat.getMainExecutor(context)) { location ->
                    location?.let { viewModel.updateUserCoordinates(it.latitude, it.longitude) }
                    onFinished()
                }
            }
        } catch (_: SecurityException) {
            onFinished()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            readCurrentLocation { isRefreshingLocation = false }
        } else {
            isRefreshingLocation = false
        }
        showLocationRationale = false
    }
    val requestLocationPermission = {
        isRefreshingLocation = true
        val fineGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (fineGranted || coarseGranted) {
            readCurrentLocation { isRefreshingLocation = false }
        } else if ((context as? Activity)?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.ACCESS_FINE_LOCATION) } == true) {
            showLocationRationale = true
        } else {
            locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    AnimatedVisibility(showLocationRationale) {
        MiraiLinkDialog(
            title = stringResource(R.string.location_permission_rationale_title),
            message = stringResource(R.string.location_permission_rationale_desc),
            onDismiss = { showLocationRationale = false },
            onAccept = {
                showLocationRationale = false
                locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            },
            onCancel = { showLocationRationale = false },
            acceptText = stringResource(R.string.accept),
            cancelText = stringResource(R.string.cancel),
        )
    }

    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MiraiLinkIconButton(onClick = onBackClick) {
                Icon(painter = androidx.compose.ui.res.painterResource(R.drawable.ic_arrow_back), contentDescription = stringResource(R.string.back))
            }
            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                MiraiLinkText(stringResource(R.string.search_settings_title), style = MaterialTheme.typography.headlineSmall)
                MiraiLinkText(stringResource(R.string.search_settings_subtitle), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.large) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(R.drawable.ic_filter_list),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(12.dp),
                )
            }
        }
        error?.let { MiraiLinkErrorContent(error = it, onAction = {}) }
        SearchSettingsSection(
            radiusKm = radiusKm,
            onRadiusChange = { radius ->
                if (scope == SearchScope.RADIUS) isMapVisible = true
                viewModel.updateDraftRadius(radius)
            },
            scope = scope,
            onScopeChange = viewModel::updateDraftScope,
            targetCountry = targetCountry,
            onTargetCountryChange = viewModel::updateDraftTargetCountry,
            matchLiveLocation = matchLiveLocation,
            onMatchLiveLocationChange = viewModel::updateDraftMatchLiveLocation,
            hasUnsavedChanges = hasUnsavedChanges,
            isSaving = isSaving,
            onSaveClick = {
                viewModel.save {
                    isMapVisible = false
                    showToast(context.getString(R.string.search_settings_saved_success), Toast.LENGTH_SHORT)
                }
            },
            latitude = latitude,
            longitude = longitude,
            isMapVisible = isMapVisible,
            onRequestLocationPermission = requestLocationPermission,
            onRefreshLocation = requestLocationPermission,
            isRefreshingLocation = isRefreshingLocation,
        )
        Spacer(Modifier.height(8.dp))
    }
}
