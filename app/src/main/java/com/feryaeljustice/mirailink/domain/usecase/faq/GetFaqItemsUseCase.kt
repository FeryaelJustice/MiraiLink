package com.feryaeljustice.mirailink.domain.usecase.faq

import com.feryaeljustice.mirailink.domain.model.faq.FaqItem
import com.feryaeljustice.mirailink.domain.repository.FaqRepository

class GetFaqItemsUseCase(
    private val repository: FaqRepository,
) {
    operator fun invoke(): List<FaqItem> = repository.getFaqItems()
}
