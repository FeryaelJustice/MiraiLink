package com.feryaeljustice.mirailink.di

import com.feryaeljustice.mirailink.state.GlobalMiraiLinkPrefs
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppEntryPoints {
    fun globalPrefs(): GlobalMiraiLinkPrefs

    fun globalSession(): GlobalMiraiLinkSession
}
