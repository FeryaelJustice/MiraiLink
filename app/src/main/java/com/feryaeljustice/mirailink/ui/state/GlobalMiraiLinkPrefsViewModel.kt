package com.feryaeljustice.mirailink.ui.state

import androidx.lifecycle.ViewModel
import com.feryaeljustice.mirailink.data.local.MiraiLinkPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GlobalMiraiLinkPrefsViewModel @Inject constructor(private val miraiLinkPrefs: MiraiLinkPrefs) :
    ViewModel() {
    suspend fun markOnboardingCompleted() = miraiLinkPrefs.markOnboardingCompleted()
    suspend fun isOnboardingCompleted() = miraiLinkPrefs.isOnboardingCompleted()
}