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
    @SerialName("explore")
    data object ExploreScreen : AppScreen()

    @Serializable
    @SerialName("category_feed")
    data class CategoryFeedScreen(
        val categoryId: String,
        val categoryName: String,
    ) : AppScreen()

    @Serializable
    @SerialName("messages")
    data object MessagesScreen : AppScreen()

    @Serializable
    @SerialName("received_likes")
    data object ReceivedLikesScreen : AppScreen()

    @Serializable
    @SerialName("user_profile_detail")
    data class UserProfileDetailScreen(
        val username: String,
        val canInteract: Boolean = true,
    ) : AppScreen()

    @Serializable
    @SerialName("chat")
    data class ChatScreen(
        val userId: String,
    ) : AppScreen()

    @Serializable
    @SerialName("aichat")
    data object AiChatScreen : AppScreen()

    @Serializable
    @SerialName("settings")
    data object SettingsScreen : AppScreen()

    @Serializable
    @SerialName("search_preferences")
    data object SearchPreferencesScreen : AppScreen()

    @Serializable
    @SerialName("profile")
    data object ProfileScreen : AppScreen()

    @Serializable
    @SerialName("feedback")
    object FeedbackScreen : AppScreen()

    @Serializable
    @SerialName("faq")
    data object FaqScreen : AppScreen()
}

private fun AppScreen.topLevelTab(): AppScreen =
    when (this) {
        is AppScreen.ChatScreen -> AppScreen.MessagesScreen
        is AppScreen.CategoryFeedScreen -> AppScreen.ExploreScreen
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
