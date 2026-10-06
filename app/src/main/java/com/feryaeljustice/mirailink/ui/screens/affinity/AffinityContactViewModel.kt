package com.feryaeljustice.mirailink.ui.screens.affinity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.model.affinity.AffinityContact
import com.feryaeljustice.mirailink.domain.usecase.affinity.AffinityUseCases
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
class AffinityContactViewModel(private val cases: AffinityUseCases) : ViewModel() {
    private val mutable = MutableStateFlow(AffinityContact())
    val contact = mutable.asStateFlow()
    private var job: Job? = null
    fun load(peer: String) {
        job?.cancel()
        mutable.value = AffinityContact()
        job = viewModelScope.launch {
            val result = cases.contact(peer)
            if (result is MiraiLinkResult.Success) mutable.value = result.data
        }
    }
}
