/**
 * @author Feryael Justice
 * @date 24/07/2024
 */
package com.feryaeljustice.mirailink.di

import com.feryaeljustice.mirailink.data.util.AndroidLogger
import com.feryaeljustice.mirailink.domain.util.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggerModule {
    @Binds
    @Singleton
    abstract fun bindLogger(impl: AndroidLogger): Logger
}
