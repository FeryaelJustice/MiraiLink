package com.feryaeljustice.mirailink.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.feryaeljustice.mirailink.domain.util.applyTelemetryConsent
import com.feryaeljustice.mirailink.ui.navigation.NavWrapper
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme
import com.feryaeljustice.mirailink.ui.utils.findActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

@Composable
fun MiraiLinkAppRoot() {
    val systemIsInDarkMode = isSystemInDarkTheme()
    var darkTheme by rememberSaveable { mutableStateOf(systemIsInDarkMode) }
//    EnableTransparentStatusBar(darkMode = darkTheme)
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    // Por defecto, desactiva telemetría hasta resolver consentimiento
    LaunchedEffect(Unit) { applyTelemetryConsent(context, false) }

    // Si ya usamos UMP aquí, añade la llamada cuando se resuelva:
    LaunchedEffect(Unit) {
        val consentInfo = UserMessagingPlatform.getConsentInformation(context)
        val params = ConsentRequestParameters.Builder()
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
                            }
                        )
                    } else {
                        updateTelemetryAndAds()
                    }
                },
                { // fallo en requestConsentInfoUpdate
                    applyTelemetryConsent(context, false)
                    MobileAds.initialize(context)
                }
            )
        } ?: run {
            // Si no hay Activity disponible → fallback seguro
            applyTelemetryConsent(context, false)
            MobileAds.initialize(context)
        }
    }

    MiraiLinkTheme(darkTheme = darkTheme) {
        NavWrapper(darkTheme = darkTheme, onThemeChange = {
            darkTheme = !darkTheme
        })
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
