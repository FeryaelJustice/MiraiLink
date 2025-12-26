package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.data.repository.AiRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.AppConfigRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.CatalogRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.ChatRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.FeedbackRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.MatchRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.OnboardingRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.ReportRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.SwipeRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.TwoFactorRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.UserRepositoryImpl
import com.feryaeljustice.mirailink.data.repository.UsersRepositoryImpl
import com.feryaeljustice.mirailink.di.koin.Qualifiers.BaseUrl
import com.feryaeljustice.mirailink.domain.repository.AiRepository
import com.feryaeljustice.mirailink.domain.repository.AppConfigRepository
import com.feryaeljustice.mirailink.domain.repository.CatalogRepository
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.repository.FeedbackRepository
import com.feryaeljustice.mirailink.domain.repository.MatchRepository
import com.feryaeljustice.mirailink.domain.repository.OnboardingRepository
import com.feryaeljustice.mirailink.domain.repository.ReportRepository
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository
import com.feryaeljustice.mirailink.domain.repository.UserRepository
import com.feryaeljustice.mirailink.domain.repository.UsersRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<AppConfigRepository> { AppConfigRepositoryImpl(get()) }
        single<AiRepository> { AiRepositoryImpl(get()) }
        single<CatalogRepository> { CatalogRepositoryImpl(get()) }
        single<ChatRepository> { ChatRepositoryImpl(get(), get(), get(BaseUrl)) }
        single<FeedbackRepository> { FeedbackRepositoryImpl(get()) }
        single<MatchRepository> { MatchRepositoryImpl(get(), get(BaseUrl)) }
        single<OnboardingRepository> { OnboardingRepositoryImpl(get()) }
        single<ReportRepository> { ReportRepositoryImpl(get()) }
        single<SwipeRepository> { SwipeRepositoryImpl(get(), get(BaseUrl)) }
        single<TwoFactorRepository> { TwoFactorRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get(), get(), get(BaseUrl)) }
        single<UsersRepository> { UsersRepositoryImpl(get(), get(BaseUrl)) }
    }
