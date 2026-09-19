package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.faq.FaqItem

interface FaqRepository {
    fun getFaqItems(): List<FaqItem>
}
