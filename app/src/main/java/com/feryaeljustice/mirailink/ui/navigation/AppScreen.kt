package com.feryaeljustice.mirailink.ui.navigation

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

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
    data object RecoverPasswordScreen : AppScreen()

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
    object SplashScreen
}

val AppScreen.route: String
    get() = Json.encodeToString(AppScreen.serializer(), this)