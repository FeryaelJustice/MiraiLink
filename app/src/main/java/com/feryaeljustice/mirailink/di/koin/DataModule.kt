// Author: Feryael Justice
// Date: 2025-11-13

package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.data.datasource.AppConfigRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.CatalogRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.ChatRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.FeedbackRemoteDatasource
import com.feryaeljustice.mirailink.data.datasource.MatchRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.ReportRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.SwipeRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.TwoFactorRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.UserRemoteDataSource
import com.feryaeljustice.mirailink.data.datasource.UsersRemoteDataSource
import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import com.feryaeljustice.mirailink.data.datastore.SessionManager
import com.feryaeljustice.mirailink.di.koin.Qualifiers.PrefsDataStore
import com.feryaeljustice.mirailink.di.koin.Qualifiers.SessionDataStore
import com.feryaeljustice.mirailink.domain.util.CredentialHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { CredentialHelper(androidContext()) }
    single { SessionManager(get(SessionDataStore)) }
    single { MiraiLinkPrefs(get(PrefsDataStore)) }

    single(createdAtStart = true) { AppConfigRemoteDataSource(get()) }
    single(createdAtStart = true) { UserRemoteDataSource(get(), androidContext()) }
    single(createdAtStart = true) { UsersRemoteDataSource(get()) }
    single(createdAtStart = true) { TwoFactorRemoteDataSource(get()) }
    single(createdAtStart = true) { SwipeRemoteDataSource(get()) }
    single(createdAtStart = true) { ChatRemoteDataSource(get()) }
    single(createdAtStart = true) { MatchRemoteDataSource(get()) }
    single(createdAtStart = true) { CatalogRemoteDataSource(get()) }
    single(createdAtStart = true) { ReportRemoteDataSource(get()) }
    single(createdAtStart = true) { FeedbackRemoteDatasource(get()) }
}
