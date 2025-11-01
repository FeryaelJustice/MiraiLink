package com.feryaeljustice.mirailink.data.datastore

import androidx.datastore.core.DataStore
import com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MiraiLinkPrefs
    @Inject
    constructor(
        private val dataStore: DataStore<AppPrefs>,
    ) {
        suspend fun markOnboardingCompleted() {
            dataStore.updateData {
                it.copy(onboardingCompleted = true)
            }
        }

        suspend fun isOnboardingCompleted(): Boolean = dataStore.data.first().onboardingCompleted
    }
