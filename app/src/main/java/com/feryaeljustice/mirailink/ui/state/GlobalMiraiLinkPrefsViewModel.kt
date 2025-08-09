package com.feryaeljustice.mirailink.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobalMiraiLinkPrefsViewModel @Inject constructor(private val miraiLinkPrefs: MiraiLinkPrefs) :
    ViewModel() {
    fun markOnboardingCompleted() =
        viewModelScope.launch { miraiLinkPrefs.markOnboardingCompleted() }
    suspend fun isOnboardingCompleted() = miraiLinkPrefs.isOnboardingCompleted()
}