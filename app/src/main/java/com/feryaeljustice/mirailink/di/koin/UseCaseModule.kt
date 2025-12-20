package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.domain.usecase.CheckAppVersionUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.AutologinUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.CheckIsVerifiedUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.LoginUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.RegisterUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.DisableTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.GetTwoFactorStatusUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.LoginVerifyTwoFactorLastStepUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.SetupTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.VerifyTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.usecase.catalog.GetAnimesUseCase
import com.feryaeljustice.mirailink.domain.usecase.catalog.GetGamesUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.ChatUseCases
import com.feryaeljustice.mirailink.domain.usecase.chat.ConnectSocketUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.CreateGroupChatUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.CreatePrivateChatUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.DisconnectSocketUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.GetChatMessagesUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.GetChatsFromUser
import com.feryaeljustice.mirailink.domain.usecase.chat.ListenForMessagesUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.MarkChatAsReadUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.SendMessageUseCase
import com.feryaeljustice.mirailink.domain.usecase.feed.GetFeedUseCase
import com.feryaeljustice.mirailink.domain.usecase.feedback.SendFeedbackUseCase
import com.feryaeljustice.mirailink.domain.usecase.match.GetMatchesUseCase
import com.feryaeljustice.mirailink.domain.usecase.notification.SaveNotificationFCMUseCase
import com.feryaeljustice.mirailink.domain.usecase.onboarding.CheckOnboardingIsCompleted
import com.feryaeljustice.mirailink.domain.usecase.photos.CheckProfilePictureUseCase
import com.feryaeljustice.mirailink.domain.usecase.photos.DeleteUserPhotoUseCase
import com.feryaeljustice.mirailink.domain.usecase.photos.UploadUserPhotoUseCase
import com.feryaeljustice.mirailink.domain.usecase.report.ReportUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.ConfirmPasswordResetUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.ConfirmVerificationCodeUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.DeleteAccountUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetUserByIdUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.RequestPasswordResetUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.RequestVerificationCodeUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.UpdateUserProfileUseCase
import org.koin.dsl.module

val useCaseModule =
    module {
        factory { CheckAppVersionUseCase(get()) }

        // Auth
        factory { AutologinUseCase(get()) }
        factory { CheckIsVerifiedUseCase(get()) }
        factory { LoginUseCase(get()) }
        factory { LogoutUseCase(get()) }
        factory { RegisterUseCase(get()) }

        // Two Factor
        factory { DisableTwoFactorUseCase(get()) }
        factory { GetTwoFactorStatusUseCase(get()) }
        factory { LoginVerifyTwoFactorLastStepUseCase(get()) }
        factory { SetupTwoFactorUseCase(get()) }
        factory { VerifyTwoFactorUseCase(get()) }

        // Catalog
        factory { GetAnimesUseCase(get()) }
        factory { GetGamesUseCase(get()) }

        // Chat
        factory { ChatUseCases(get(), get(), get(), get(), get(), get(), get()) }
        factory { ConnectSocketUseCase(get()) }
        factory { CreateGroupChatUseCase(get()) }
        factory { CreatePrivateChatUseCase(get()) }
        factory { DisconnectSocketUseCase(get()) }
        factory { GetChatMessagesUseCase(get()) }
        factory { GetChatsFromUser(get()) }
        factory { ListenForMessagesUseCase(get()) }
        factory { MarkChatAsReadUseCase(get()) }
        factory { SendMessageUseCase(get()) }

        // Feed
        factory { GetFeedUseCase(get()) }

        // Feedback
        factory { SendFeedbackUseCase(get()) }

        // Match
        factory { GetMatchesUseCase(get()) }

        // Notification
        factory { SaveNotificationFCMUseCase(get()) }

        // Onboarding
        factory { CheckOnboardingIsCompleted(get()) }

        // Photos
        factory { CheckProfilePictureUseCase(get()) }
        factory { DeleteUserPhotoUseCase(get()) }
        factory { UploadUserPhotoUseCase(get()) }

        // Report
        factory { ReportUseCase(get()) }

        // Swipe
        factory { DislikeUserUseCase(get()) }
        factory { LikeUserUseCase(get()) }

        // Users
        factory { ConfirmPasswordResetUseCase(get()) }
        factory { ConfirmVerificationCodeUseCase(get()) }
        factory { DeleteAccountUseCase(get()) }
        factory { GetCurrentUserUseCase(get()) }
        factory { GetUserByIdUseCase(get()) }
        factory { RequestPasswordResetUseCase(get()) }
        factory { RequestVerificationCodeUseCase(get()) }
        factory { UpdateUserProfileUseCase(get()) }
    }
