package com.feryaeljustice.mirailink.di.koin

import android.util.Log
import com.feryaeljustice.mirailink.core.remoteconfig.RemoteConfigManager
import com.feryaeljustice.mirailink.di.koin.Qualifiers.IoDispatcher
import com.feryaeljustice.mirailink.di.koin.Qualifiers.MainDispatcher
import com.feryaeljustice.mirailink.ui.navigation.NavAnalyticsViewModel
import com.feryaeljustice.mirailink.ui.screens.ai.chat.AiChatViewModel
import com.feryaeljustice.mirailink.ui.screens.auth.AuthViewModel
import com.feryaeljustice.mirailink.ui.screens.auth.recover.RecoverPasswordViewModel
import com.feryaeljustice.mirailink.ui.screens.auth.verification.VerificationViewModel
import com.feryaeljustice.mirailink.ui.screens.chat.ChatViewModel
import com.feryaeljustice.mirailink.ui.screens.home.HomeViewModel
import com.feryaeljustice.mirailink.ui.screens.messages.MessagesViewModel
import com.feryaeljustice.mirailink.ui.screens.photo.ProfilePictureViewModel
import com.feryaeljustice.mirailink.ui.screens.profile.ProfileViewModel
import com.feryaeljustice.mirailink.ui.screens.settings.SettingsViewModel
import com.feryaeljustice.mirailink.ui.screens.settings.feedback.FeedbackViewModel
import com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure.ConfigureTwoFactorViewModel
import com.feryaeljustice.mirailink.ui.screens.splash.SplashScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel {
            val remoteConfigManager: RemoteConfigManager = get()
            val isInChristmasMode = remoteConfigManager.getIsChristmasMode()
            Log.d("isInChristmasMode", "Christmas mode enabled: $isInChristmasMode")

            SplashScreenViewModel(
                checkAppVersionUseCase = get(),
                autologinUseCase = get(),
                checkOnboardingIsCompletedUseCase = get(),
                ioDispatcher = get(qualifier = IoDispatcher),
                mainDispatcher = get(qualifier = MainDispatcher),
                store = get(),
                isInChristmasMode = isInChristmasMode,
            )
        }
        viewModel {
            AuthViewModel(
                loginUseCase = lazy { get() },
                registerUseCase = lazy { get() },
                getTwoFactorStatusUseCase = lazy { get() },
                loginVerifyTwoFactorLastStepUseCase = lazy { get() },
                analytics = lazy { get() },
                crash = lazy { get() },
                credentialHelper = lazy { get() },
                ioDispatcher = get(qualifier = IoDispatcher),
                mainDispatcher = get(qualifier = MainDispatcher),
            )
        }
        viewModel {
            RecoverPasswordViewModel(
                requestResetUseCase = get(),
                confirmResetUseCase = get(),
                ioDispatcher = get(qualifier = IoDispatcher),
            )
        }
        viewModel {
            VerificationViewModel(
                checkIsVerifiedUseCase = get(),
                requestCodeUseCase = get(),
                confirmCodeUseCase = get(),
                ioDispatcher = get(qualifier = IoDispatcher),
            )
        }
        viewModel {
            ChatViewModel(
                createPrivateChatUseCase = get(),
                createGroupChatUseCase = get(),
                getChatMessagesUseCase = get(),
                markChatAsReadUseCase = get(),
                sendMessageUseCase = get(),
                getCurrentUserUseCase = get(),
                getUserByIdUseCase = get(),
                reportUseCase = get(),
                logger = get(),
                ioDispatcher = get(qualifier = IoDispatcher),
            )
        }
        viewModel {
            AiChatViewModel(
                generateContentUseCase = get(),
                ioDispatcher = get(qualifier = IoDispatcher),
            )
        }
        viewModel {
            HomeViewModel(
                getFeedUseCase = get(),
                likeUser = get(),
                dislikeUser = get(),
                getCurrentUserUseCase = get(),
                ioDispatcher = get(qualifier = IoDispatcher),
            )
        }
        viewModel {
            MessagesViewModel(
                getMatchesUseCase = get(),
                chatUseCases = get(),
                ioDispatcher = get(qualifier = IoDispatcher),
            )
        }
        viewModel {
            ProfilePictureViewModel(
                uploadUserPhotoUseCase = get(),
                ioDispatcher = get(qualifier = IoDispatcher),
            )
        }
        viewModel {
            ProfileViewModel(
                getCurrentUserUseCase = get(),
                updateUserProfileUseCase = get(),
                deleteUserPhotoUseCase = get(),
                getAnimesUseCase = get(),
                getGamesUseCase = get(),
                logger = get(),
                ioDispatcher = get(qualifier = IoDispatcher),
            )
        }
        viewModel {
            SettingsViewModel(
                logoutUseCase = get(),
                deleteAccountUseCase = get(),
                ioDispatcher = get(qualifier = IoDispatcher),
                mainDispatcher = get(qualifier = MainDispatcher),
            )
        }
        viewModel {
            FeedbackViewModel(
                sendFeedbackUseCase = get(),
                ioDispatcher = get(qualifier = IoDispatcher),
            )
        }
        viewModel {
            ConfigureTwoFactorViewModel(
                setup2FAUseCase = get(),
                verifyTwoFactorUseCase = get(),
                getTwoFactorStatusUseCase = get(),
                disableTwoFactorUseCase = get(),
                ioDispatcher = get(qualifier = IoDispatcher),
            )
        }
        viewModelOf(::NavAnalyticsViewModel)
    }
