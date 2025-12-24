package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStore
import com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStoreImpl
import com.feryaeljustice.mirailink.ui.theme.AppThemeManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val featureFlagModule =
    module {
        single<FeatureFlagStore> {
            FeatureFlagStoreImpl(androidContext())
        }

        single {
            AppThemeManager(
                featureFlagStore = get<FeatureFlagStore>(),
                context = androidContext(),
            )
        }
    }
