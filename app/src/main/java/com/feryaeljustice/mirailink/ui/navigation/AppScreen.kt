package com.feryaeljustice.mirailink.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ScreensSubgraphs {
    @Serializable
    data object Auth : ScreensSubgraphs()

    @Serializable
    data object Main : ScreensSubgraphs()
}

@Serializable
sealed class AppScreen {
    @Serializable
    data object AuthScreen : AppScreen()

    @Serializable
    data class RecoverPasswordScreen(val email: String = "") : AppScreen()

    @Serializable
    data class VerificationScreen(val userId: String = "") : AppScreen()

    @Serializable
    data object ProfilePictureScreen : AppScreen()

    @Serializable
    data object HomeScreen : AppScreen()

    @Serializable
    data object MessagesScreen : AppScreen()

    @Serializable
    data class ChatScreen(val userId: String) : AppScreen()

    @Serializable
    data object SettingsScreen : AppScreen()

    @Serializable
    data object ProfileScreen : AppScreen()

    @Serializable
    object FeedbackScreen : AppScreen()

    @Serializable
    object SplashScreen
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
