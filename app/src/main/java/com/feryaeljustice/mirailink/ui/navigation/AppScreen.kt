package com.feryaeljustice.mirailink.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ScreensSubgraphs : NavKey {
    @Serializable
    @SerialName("sg_auth")
    data object Auth : ScreensSubgraphs()

    @Serializable
    @SerialName("sg_main")
    data object Main : ScreensSubgraphs()
}

@Serializable
sealed class AppScreen : NavKey {
    @Serializable
    @SerialName("splash")
    object SplashScreen : AppScreen()

    @Serializable
    @SerialName("onboarding")
    object OnboardingScreen : AppScreen()

    @Serializable
    @SerialName("auth")
    data object AuthScreen : AppScreen()

    @Serializable
    @SerialName("recover_password")
    data class RecoverPasswordScreen(
        val email: String = "",
    ) : AppScreen()

    @Serializable
    @SerialName("verification")
    data class VerificationScreen(
        val userId: String = "",
    ) : AppScreen()

    @Serializable
    @SerialName("profile_picture")
    data object ProfilePictureScreen : AppScreen()

    @Serializable
    @SerialName("home")
    data object HomeScreen : AppScreen()

    @Serializable
    @SerialName("messages")
    data object MessagesScreen : AppScreen()

    @Serializable
    @SerialName("chat")
    data class ChatScreen(
        val userId: String,
    ) : AppScreen()

    @Serializable
    @SerialName("settings")
    data object SettingsScreen : AppScreen()

    @Serializable
    @SerialName("profile")
    data object ProfileScreen : AppScreen()

    @Serializable
    @SerialName("feedback")
    object FeedbackScreen : AppScreen()

    @Serializable
    @SerialName("configure_2fa")
    object ConfigureTwoFactorScreen : AppScreen()
}

private fun AppScreen.topLevelTab(): AppScreen =
    when (this) {
        is AppScreen.ChatScreen -> AppScreen.MessagesScreen
        else -> this
    }

/*
val AppScreen.route: String
    get() = Json.encodeToString(AppScreen.serializer(), this)

val String.decodedRoute: String
    get() {
        val jsonElement = Json.parseToJsonElement(this)
        return jsonElement.jsonObject["type"]?.jsonPrimitive?.content
            ?: error("Missing 'type' field in encoded AppScreen")
    }*/
