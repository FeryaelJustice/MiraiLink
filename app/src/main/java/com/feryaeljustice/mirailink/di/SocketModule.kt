package com.feryaeljustice.mirailink.di

import com.feryaeljustice.mirailink.data.remote.socket.SocketService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketModule {

    @Provides
    @Singleton
    fun provideSocketService(@Named("BaseUrl") baseUrl: String): SocketService {
        val socketService = SocketService()
        socketService.initSocket(baseUrl)
        return socketService
    }
}