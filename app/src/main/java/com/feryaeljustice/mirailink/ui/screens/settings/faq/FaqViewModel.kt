package com.feryaeljustice.mirailink.ui.screens.settings.faq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.model.faq.FaqCategory
import com.feryaeljustice.mirailink.domain.model.faq.FaqItem
import com.feryaeljustice.mirailink.domain.usecase.faq.GetFaqItemsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FaqViewModel(
    private val getFaqItemsUseCase: GetFaqItemsUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _faqItems = MutableStateFlow<List<FaqItem>>(emptyList())
    val faqItems: StateFlow<List<FaqItem>> = _faqItems.asStateFlow()

    private val _expandedQuestionIds = MutableStateFlow<Set<String>>(emptySet())
    val expandedQuestionIds: StateFlow<Set<String>> = _expandedQuestionIds.asStateFlow()

    private val _selectedCategory = MutableStateFlow<FaqCategory?>(null)
    val selectedCategory: StateFlow<FaqCategory?> = _selectedCategory.asStateFlow()

    init {
        loadFaqItems()
    }

    private fun loadFaqItems() {
        viewModelScope.launch(ioDispatcher) {
            _faqItems.value = getFaqItemsUseCase()
        }
    }

    fun toggleItemExpansion(itemId: String) {
        val current = _expandedQuestionIds.value
        _expandedQuestionIds.value = if (current.contains(itemId)) {
            current - itemId
        } else {
            current + itemId
        }
    }

    fun selectCategory(category: FaqCategory?) {
        _selectedCategory.value = category
    }
}
