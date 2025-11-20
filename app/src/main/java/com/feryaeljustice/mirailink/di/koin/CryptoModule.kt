// Author: Feryael Justice
// Date: 2024-07-31

package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.data.datastore.crypto.KeystoreAesGcmProvider
import com.feryaeljustice.mirailink.data.datastore.crypto.SecretKeyProvider
import org.koin.dsl.module

val cryptoModule =
    module {
        single<SecretKeyProvider> {
            KeystoreAesGcmProvider(alias = "ml_aes_gcm_v1")
        }
    }
