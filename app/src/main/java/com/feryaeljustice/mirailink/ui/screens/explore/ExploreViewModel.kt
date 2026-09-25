package com.feryaeljustice.mirailink.ui.screens.explore

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.repository.ExploreHubData
import com.feryaeljustice.mirailink.domain.usecase.explore.GetExploreSectionsUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ExploreViewModel(
    private val getExploreSectionsUseCase: GetExploreSectionsUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : RetryableViewModel() {

    sealed class ExploreUiState {
        data object Idle : ExploreUiState()
        data object Loading : ExploreUiState()
        data class Success(val hubData: ExploreHubData) : ExploreUiState()
        data class Error(val error: UiError) : ExploreUiState()
    }

    private val _state = MutableStateFlow<ExploreUiState>(ExploreUiState.Idle)
    val state: StateFlow<ExploreUiState> = _state.asStateFlow()

    init {
        loadExploreHub()
    }

    fun loadExploreHub() {
        viewModelScope.launch {
            _state.value = ExploreUiState.Loading
            when (val result = withContext(ioDispatcher) { getExploreSectionsUseCase() }) {
                is MiraiLinkResult.Success -> {
                    _state.value = ExploreUiState.Success(result.data)
                }
                is MiraiLinkResult.Error -> {
                    setRecoveryAction(::loadExploreHub)
                    _state.value = ExploreUiState.Error(result.error.toUiError())
                }
            }
        }
    }
}
