package com.feryaeljustice.mirailink.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.feryaeljustice.mirailink.core.featureflags.FeatureFlag
import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import com.feryaeljustice.mirailink.domain.util.applyTelemetryConsent
import com.feryaeljustice.mirailink.ui.components.notifications.NotificationRationaleDialog
import com.feryaeljustice.mirailink.ui.navigation.NavWrapper
import com.feryaeljustice.mirailink.ui.theme.AppThemeManager
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme
import com.feryaeljustice.mirailink.ui.utils.findActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Suppress("EffectKeys", "ktlint:standard:function-naming")
@Composable
fun MiraiLinkAppRoot(
    appThemeManager: AppThemeManager,
    flags: Map<String, FeatureFlag>,
    themePreference: ThemePreference = ThemePreference.SYSTEM,
    modifier: Modifier = Modifier,
) {
    val systemIsInDarkMode = isSystemInDarkTheme()
    val isDark = when (themePreference) {
        ThemePreference.SYSTEM -> systemIsInDarkMode
        ThemePreference.LIGHT -> false
        ThemePreference.DARK -> true
    }
//    EnableTransparentStatusBar(darkMode = darkTheme)

    // --- SETUP GENERAL Y CONTEXTO ---
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val canRequestNotifications =
        remember { true } // Before: Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    // ---------------------------------------------------------------------------------------------
    // 🎯 SECCIÓN DE GESTIÓN DE PERMISOS DE NOTIFICACIONES (FCM)
    // ---------------------------------------------------------------------------------------------

    // Estado para controlar si mostramos el diálogo Rationale de Compose
    var showNotificationRationaleDialog by remember { mutableStateOf(false) }

    // El Launcher de permisos de Compose (usa el contrato estándar de Android)
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("FCM", "Permiso de notificación otorgado")
            } else {
                Log.i("FCM", "Permiso de notificación denegado")
            }
            showNotificationRationaleDialog = false // Asegurarse de cerrar el diálogo
        }

    // Lógica para determinar si pedir o explicar el permiso
    val askNotificationPermission: () -> Unit = askNotificationPermission@{
        if (!canRequestNotifications) return@askNotificationPermission
        when {
            // 1. Permiso ya concedido
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("FCM", "Permiso de notificación: YA otorgado.")
            }

            // 2. Debemos mostrar la explicación Rationale (Rechazo temporal)
            shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.POST_NOTIFICATIONS,
            ) -> {
                showNotificationRationaleDialog = true
            }

            // 3. Pide el permiso (Primera vez o rechazo permanente)
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // 🚀 Lanzamiento al inicio del componente: Pide el permiso de forma asíncrona
    LaunchedEffect(Unit) {
        askNotificationPermission()
    }

    // 4. La Composable que muestra el Diálogo Rationale si el estado lo indica
    if (showNotificationRationaleDialog) {
        NotificationRationaleDialog(
            onAccept = {
                // Si el usuario acepta la explicación, lanzamos la petición real
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            onDismiss = {
                showNotificationRationaleDialog = false
            },
        )
    }

    // ---------------------------------------------------------------------------------------------
    // ❌ SECCIÓN CONSENTIMIENTO UMP
    // ---------------------------------------------------------------------------------------------

    // Por defecto, desactiva telemetría hasta resolver consentimiento
    LaunchedEffect(Unit) { applyTelemetryConsent(context, false) }

    // Si ya usamos UMP aquí, añade la llamada cuando se resuelva:
    LaunchedEffect(Unit) {
        val consentInfo = UserMessagingPlatform.getConsentInformation(context)
        val params =
            ConsentRequestParameters
                .Builder()
                .setTagForUnderAgeOfConsent(false)
                .build()

        activity?.let { act ->
            consentInfo.requestConsentInfoUpdate(
                act,
                params,
                {
                    val updateTelemetryAndAds: () -> Unit = {
                        val consentGiven =
                            consentInfo.consentStatus == ConsentInformation.ConsentStatus.OBTAINED ||
                                consentInfo.consentStatus == ConsentInformation.ConsentStatus.NOT_REQUIRED
                        applyTelemetryConsent(context, consentGiven)
                        MobileAds.initialize(context)
                    }

                    if (consentInfo.isConsentFormAvailable &&
                        consentInfo.consentStatus == ConsentInformation.ConsentStatus.REQUIRED
                    ) {
                        UserMessagingPlatform.loadConsentForm(
                            context,
                            { form ->
                                form.show(act) { updateTelemetryAndAds() }
                            },
                            { _ ->
                                updateTelemetryAndAds()
                            },
                        )
                    } else {
                        updateTelemetryAndAds()
                    }
                },
                {
                    // fallo en requestConsentInfoUpdate
                    applyTelemetryConsent(context, false)
                    MobileAds.initialize(context)
                },
            )
        } ?: run {
            // Si no hay Activity disponible → fallback seguro
            applyTelemetryConsent(context, false)
            MobileAds.initialize(context)
        }
    }

    // ---------------------------------------------------------------------------------------------
    // 🎨 ESTRUCTURA PRINCIPAL DE LA UI
    // ---------------------------------------------------------------------------------------------

    MiraiLinkTheme(
        themeMode = appThemeManager.getThemeMode(flags),
        darkTheme = isDark,
        modifier = modifier,
    ) {
        NavWrapper()
    }
}

/*
@Composable
private fun EnableTransparentStatusBar(darkMode: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        val colorTransparent = Color.Transparent.toArgb()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                view.setBackgroundColor(colorTransparent)
                insets
            }
        } else {
            window.statusBarColor = colorTransparent
        }
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkMode
    }
}*/
