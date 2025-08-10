package com.feryaeljustice.mirailink.di

import com.feryaeljustice.mirailink.data.datastore.crypto.KeystoreAesGcmProvider
import com.feryaeljustice.mirailink.data.datastore.crypto.SecretKeyProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CryptoModule {
    @Provides
    @Singleton
    fun provideSecretKeyProvider(): SecretKeyProvider =
        KeystoreAesGcmProvider(alias = "ml_aes_gcm_v1")

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}